package com.everest.hibiscus.api.modules.compat.internal;

import com.everest.hibiscus.api.modules.compat.annotation.ConditionalUnavailableException;
import com.everest.hibiscus.api.modules.compat.annotation.RequiresMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ConditionalProcessor {

    private static final Map<Member, Boolean> memberAvailability = new HashMap<>();
    private static final Map<Class<?>, Boolean> classAvailability = new HashMap<>();

    // ---------------------------------------------------------------------
    // Init (called ONCE)
    // ---------------------------------------------------------------------
    public static void initialize(String basePackage) {
        try {
            scanPackage(basePackage);
        } catch (Exception e) {
            throw new RuntimeException("ConditionalProcessor failed", e);
        }
    }

    // ---------------------------------------------------------------------
    // Scan package
    // ---------------------------------------------------------------------
    private static void scanPackage(String pkg) throws Exception {
        String path = pkg.replace('.', '/');

        Enumeration<URL> roots = Thread.currentThread()
                .getContextClassLoader()
                .getResources(path);

        while (roots.hasMoreElements()) {
            URL root = roots.nextElement();
            File f = new File(root.getFile());
            scanDirectory(f, pkg);
        }
    }

    private static void scanDirectory(File dir, String pkg) {
        if (!dir.exists()) return;

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file, pkg + "." + file.getName());
                continue;
            }

            if (file.getName().endsWith(".class")) {
                String className = pkg + "." + file.getName().replace(".class", "");
                processClass(className);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Process a class
    // ---------------------------------------------------------------------
    private static void processClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);

            // CLASS
            if (clazz.isAnnotationPresent(RequiresMod.class)) {
                RequiresMod req = clazz.getAnnotation(RequiresMod.class);
                boolean ok = evaluate(req);
                classAvailability.put(clazz, ok);

                if (!ok) {
                    System.out.println("[Conditional] Skipping class: " + className);
                    return;
                }
            }

            // FIELDS
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(RequiresMod.class)) {
                    RequiresMod req = f.getAnnotation(RequiresMod.class);
                    boolean ok = evaluate(req);
                    memberAvailability.put(f, ok);
                }
            }

            // METHODS
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(RequiresMod.class)) {
                    RequiresMod req = m.getAnnotation(RequiresMod.class);
                    boolean ok = evaluate(req);
                    memberAvailability.put(m, ok);
                }
            }

        } catch (Throwable ignored) {}
    }

    // ---------------------------------------------------------------------
    // Evaluation logic (mods + version matching)
    // ---------------------------------------------------------------------
    public static boolean evaluate(RequiresMod req) {
        String[] mods = req.mods().length > 0 ?
                req.mods() : new String[]{req.mod()};

        String[] versions = req.versions();
        if (versions.length == 0)
            versions = new String[mods.length]; // all empty → any version

        RequiresMod.Policy p = req.policy();

        int okCount = 0;

        for (int i = 0; i < mods.length; i++) {
            String mod = mods[i];
            String verConstraint = i < versions.length ? versions[i] : "";

            boolean loaded = FabricLoader.getInstance().isModLoaded(mod);
            if (!loaded) {
                if (p == RequiresMod.Policy.ALL) return false;
                if (p == RequiresMod.Policy.NONE) okCount++;
                continue;
            }

            // mod is loaded → check version if provided
            String modVersion = FabricLoader.getInstance()
                    .getModContainer(mod).get()
                    .getMetadata().getVersion().getFriendlyString();

            boolean versionOK = VersionMatcher.matches(modVersion, verConstraint);

            if (!versionOK && p == RequiresMod.Policy.ALL) return false;

            if (versionOK) okCount++;
        }

        return switch (p) {
            case ANY -> okCount > 0;
            case ALL -> okCount == mods.length;
            case NONE -> okCount == 0;
        };
    }

    // ---------------------------------------------------------------------
    // Access checks
    // ---------------------------------------------------------------------
    public static void checkAvailable(Member m) {
        Boolean ok = memberAvailability.get(m);
        if (ok != null && !ok) {
            RequiresMod req = ((AnnotatedElement) m).getAnnotation(RequiresMod.class);
            throw new ConditionalUnavailableException(req.mods().length > 0
                    ? req.mods() : new String[]{req.mod()},
                    req.reason());
        }
    }

    public static void checkClassAvailable(Class<?> c) {
        Boolean ok = classAvailability.get(c);
        if (ok != null && !ok) {
            RequiresMod req = c.getAnnotation(RequiresMod.class);
            throw new ConditionalUnavailableException(req.mods().length > 0
                    ? req.mods() : new String[]{req.mod()},
                    req.reason());
        }
    }
}
