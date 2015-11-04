package com.tunebrains.android.versioning.internal

import org.gradle.api.Project

/**
 * Created by alex on 11/4/15.
 */
class VersionUpdater {
    Project project
    File versionPropsFile

    VersionUpdater(Project project) {
        this.project = project
        versionPropsFile = new File(project.buildFile.getParent() + "/version.properties")
    }

    def setDefault(String pKey, int pValue) {
        System.out.println("Write default for " + pKey + " to " + pValue)
        def Properties versionProps = new Properties()
        versionProps.load(new FileInputStream(versionPropsFile))
        if (versionProps[keyName(pKey)] == null) {
            versionProps[keyName(pKey)] = pValue.toString()
        }
        versionProps.store(versionPropsFile.newWriter(), null)
    }

    String keyName(String s) {
        String.format("VERSION_%s", s)
    }

    def increasePatch() {
        int patch = patch()
        System.out.println("Was Patch: " + patch)
        patch += 1
        write("PATCH", patch)
        System.out.println("Set Patch: " + patch)
    }

    void write(String pKey, int pValue) {
        def Properties versionProps = new Properties()
        versionProps.load(new FileInputStream(versionPropsFile))
        versionProps[keyName(pKey)] = pValue.toString()
        versionProps.store(versionPropsFile.newWriter(), null)
    }

    int major() {
        return Integer.parseInt(keyValue("MAJOR"))
    }

    int minor() {
        return Integer.parseInt(keyValue("MINOR"))
    }

    int patch() {
        return Integer.parseInt(keyValue("PATCH"))
    }

    String keyValue(String pKey) {
        def Properties versionProps = new Properties()
        versionProps.load(new FileInputStream(versionPropsFile))
        def v = versionProps[keyName(pKey)]
        System.out.println("Read value for " + pKey + " eql " + v.toString())
        v
    }
}
