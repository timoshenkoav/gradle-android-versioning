package com.tunebrains.android.versioning.internal

import org.gradle.api.Project

/**
 * Created by alex on 11/4/15.
 */
class VersionOptions {
    private int major;
    private int minor;
    private int patch;
    private final Project project;
    VersionUpdater versionUpdater

    public VersionOptions(Project pProject, VersionUpdater lVersionUpdater) {
        project = pProject;
        versionUpdater = lVersionUpdater;

    }

    public major(int v) {
        versionUpdater.setDefault("MAJOR", v)
    }

    public minor(int v) {
        versionUpdater.setDefault("MINOR", v)
    }

    public patch(int v) {
        versionUpdater.setDefault("PATCH", v)
    }

    int getVersionCode() {
        read()
        return major * 100000 + minor * 1000 + patch
    }

    String getVersionName() {
        read()
        String.format("%d.%d.%d", major, minor, patch)
    }

    def read() {
        update()
    }

    def update() {
        major = versionUpdater.major()
        minor = versionUpdater.minor()
        patch = versionUpdater.patch()
    }
}
