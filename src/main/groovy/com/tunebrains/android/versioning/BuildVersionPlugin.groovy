package com.tunebrains.android.versioning

import com.tunebrains.android.versioning.internal.BuildVersionExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by alex on 11/3/15.
 */
class BuildVersionPlugin implements Plugin<Project> {

    void apply(Project project) {
        def androidGradlePlugin = getAndroidPluginVersion(project)
        if (androidGradlePlugin == null) {
            throw new IllegalStateException("The Android Gradle plugin not found. the " +
                    "\"advanced-build-version\" plugin only works with Android gradle library.")
        } else if (!checkAndroidVersion(androidGradlePlugin.version)) {
            throw new IllegalStateException("The Android Gradle plugin ${androidGradlePlugin.version} is not supported.")
        }

        def advancedVersioning = project.extensions.create("advancedVersioning", BuildVersionExtension, project)

        project.afterEvaluate {
            def needIncrease = advancedVersioning.codeOptions.needIncrease()

            def wasRelease = false
            for (dependentTask in advancedVersioning.nameOptions.minorDependsOnTasks) {
                for (String taskName in project.gradle.startParameter.taskNames) {
                    if (taskName.toLowerCase(Locale.ENGLISH).contains(dependentTask.toLowerCase(Locale.ENGLISH))) {
                        wasRelease = true
                    }
                }
            }

            if (needIncrease) {
                advancedVersioning.codeOptions.increase(advancedVersioning)
                advancedVersioning.nameOptions.increasePatch(advancedVersioning)
            }
            if (wasRelease) {
                advancedVersioning.nameOptions.increaseMinor(advancedVersioning)
            }
        }
    }

    private static final String[] SUPPORTED_ANDROID_VERSIONS = ['0.14.', '1.'];

    def static boolean checkAndroidVersion(String version) {
        for (String supportedVersion : SUPPORTED_ANDROID_VERSIONS) {
            if (version.startsWith(supportedVersion)) {
                return true
            }
        }

        return false
    }

    def static getAndroidPluginVersion(Project project) {
        def projectGradle = findClassPathDependencyVersion(project, 'com.android.tools.build', 'gradle')
        if (projectGradle == null) {
            projectGradle = findClassPathDependencyVersion(project.getRootProject(), 'com.android.tools.build', 'gradle')
        }
        return projectGradle
    }

    def static findClassPathDependencyVersion(Project project, group, attributeId) {
        return project.buildscript.configurations.classpath.dependencies.find {
            it.group != null && it.group.equals(group) && it.name.equals(attributeId)
        }
    }
}