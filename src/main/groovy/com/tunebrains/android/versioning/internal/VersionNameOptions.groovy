package com.tunebrains.android.versioning.internal

import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Created by alex on 11/3/15.
 */
class VersionNameOptions {
    private static final DEFAULT_DEPENDS_ON = Collections.singletonList('debug')
    private static final RELEASE_DEFAULT_DEPENDS_ON = Collections.singletonList('release')
    List<Object> minorDependsOnTasks = RELEASE_DEFAULT_DEPENDS_ON
    List<Object> patchDependsOnTasks = DEFAULT_DEPENDS_ON
    private File versionPropsFile

    private Project project

    VersionNameOptions(Project project) {
        this.project = project
        versionPropsFile = new File(project.buildFile.getParent() + "/version.properties")
    }

    void minorDependsOnTasks(Object... paths) {
        this.minorDependsOnTasks = Arrays.asList(paths)
    }

    void patchDependsOnTasks(Object... paths) {
        this.patchDependsOnTasks = Arrays.asList(paths)
    }

    File getVersionFile() {
        return versionPropsFile
    }

    String getVersionName() {

        versionPropsFile = new File(project.buildFile.getParent() + "/version.properties")
        if (versionPropsFile.canRead()) {
            def major = "0", minor = "0", patch = "1"

            def Properties versionProps = new Properties()
            versionProps.load(new FileInputStream(versionPropsFile))
            if (versionProps['AI_VERSION_NAME_MAJOR'] == null) {
                versionProps['AI_VERSION_NAME_MAJOR'] = "0"
            } else {
                major = versionProps['AI_VERSION_NAME_MAJOR']
            }
            if (versionProps['AI_VERSION_NAME_MINOR'] == null) {
                versionProps['AI_VERSION_NAME_MINOR'] = "0"
            } else {
                minor = versionProps['AI_VERSION_NAME_MINOR']
            }
            if (versionProps['AI_VERSION_NAME_PATCH'] == null) {
                versionProps['AI_VERSION_NAME_PATCH'] = "1"
            } else {
                patch = versionProps['AI_VERSION_NAME_PATCH']
            }
            System.out.println("major: " + major)
            System.out.println("minor: " + minor)
            System.out.println("patch: " + patch)
            return String.format("%s.%s.%s", major, minor, patch)

        } else {
            throw new GradleException("Could not read version.properties file in path \""
                    + versionPropsFile.getAbsolutePath() + "\" \r\n" +
                    "Please create this file and add it to your VCS (git, svn, ...).")
        }
    }

    def increasePatch(BuildVersionExtension advancedVersioning) {
        System.out.println("Will increase patch version name")
        def versionPropsFile = versionFile
        if (versionPropsFile.canRead()) {
            def Properties versionProps = new Properties()
            versionProps.load(new FileInputStream(versionPropsFile))
            def versionCode = 2
            def rawVersion = versionProps['AI_VERSION_NAME_PATCH']
            System.out.println("Plugin: raw version:" + rawVersion)
            if (rawVersion != null) {
                versionCode = Integer.parseInt(rawVersion)
                versionCode += 1
            }
            versionProps['AI_VERSION_NAME_PATCH'] = versionCode.toString()
            versionProps.store(versionPropsFile.newWriter(), null)

        }
    }

    def increaseMinor(BuildVersionExtension pBuildVersionExtension) {
        System.out.println("Will increase minor version name")
        def versionPropsFile = versionFile
        if (versionPropsFile.canRead()) {
            def Properties versionProps = new Properties()
            versionProps.load(new FileInputStream(versionPropsFile))

            versionProps['AI_VERSION_NAME_PATCH'] = "0"
            def versionCode = 1
            def rawVersion = versionProps['AI_VERSION_NAME_MINOR']
            if (rawVersion != null) {
                versionCode = Integer.parseInt(rawVersion)
                versionCode += 1
            }
            versionProps['AI_VERSION_NAME_MINOR'] = versionCode.toString()
            versionProps.store(versionPropsFile.newWriter(), null)

        }
    }
}
