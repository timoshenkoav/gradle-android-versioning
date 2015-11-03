package com.tunebrains.android.versioning.internal

import org.gradle.api.Project

import javax.inject.Inject;

/**
 * Created by alex on 11/3/15.
 */
class BuildVersionExtension {
    final Project project;

    VersionNameOptions nameOptions
    VersionCodeOptions codeOptions
    FileOutputOptions outputOptions


    @Inject
    BuildVersionExtension(Project pProject) {
        this.project = pProject
        nameOptions = new VersionNameOptions(project)
        codeOptions = new VersionCodeOptions(project)
        outputOptions = new FileOutputOptions()
    }

    void nameOptions(Closure c) {
        project.configure(nameOptions, c)
    }

    void codeOptions(Closure c) {
        project.configure(codeOptions, c)
    }

    void outputOptions(Closure c) {
        project.configure(outputOptions, c)
    }

    int getVersionCode() {
        int vName = codeOptions.versionCode
        System.out.println("Get Version Code: " + vName)
        return vName
    }

    String getVersionName() {
        String vName = nameOptions.versionName
        System.out.println("Get Version Name: " + vName)
        return vName
    }

}
