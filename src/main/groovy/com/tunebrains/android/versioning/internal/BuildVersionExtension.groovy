package com.tunebrains.android.versioning.internal

import org.gradle.api.Project

import javax.inject.Inject;

/**
 * Created by alex on 11/3/15.
 */
class BuildVersionExtension {
    final Project project;

    VersionOptions versionOptions;

    private boolean increaseAfterBuild;
    def wasIncreased
    VersionUpdater versionUpdater

    @Inject
    BuildVersionExtension(Project pProject) {
        this.project = pProject
        this.versionUpdater = new VersionUpdater(pProject)
        versionOptions = new VersionOptions(pProject, versionUpdater)
    }

    public void increaseAfterBuild(boolean v) {
        System.out.println("Set increase after build")
        this.increaseAfterBuild = v;
    }

    void versionOptions(Closure c) {
        project.configure(versionOptions, c)
    }

    int getVersionCode() {
        checkIncrease(false)
        def curVersion = versionOptions.getVersionCode()
        System.out.println("getVersionCode " + curVersion)
        return curVersion
    }

    def checkIncrease(boolean isafter) {
        def needIncrease = false
        if (increaseAfterBuild && isafter) {
            needIncrease = true
        }
        if (!increaseAfterBuild && !isafter) {
            needIncrease = true
        }
        if (needIncrease) {
            increase()
        }
    }

    def increase() {
        if (wasIncreased)
            return
        System.out.println("Increase version")
        versionUpdater.increasePatch()
        versionOptions.update()
        wasIncreased = true;
    }

    String getVersionName() {
        checkIncrease(false)
        System.out.println("getVersionName " + versionOptions.getVersionName())
        return versionOptions.getVersionName();
    }

}
