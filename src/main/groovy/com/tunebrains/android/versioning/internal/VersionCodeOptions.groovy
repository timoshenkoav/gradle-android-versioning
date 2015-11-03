package com.tunebrains.android.versioning.internal

import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Created by alex on 11/3/15.
 */
class VersionCodeOptions {
    private static final DEFAULT_DEPENDS_ON = Collections.singletonList('debug')
    List<Object> dependsOnTasks = DEFAULT_DEPENDS_ON
    private File versionPropsFile

    private Project project

    VersionCodeOptions(Project project) {
        this.project = project
        versionPropsFile = new File(project.buildFile.getParent() + "/version.properties")
    }

    void dependsOnTasks(Object... paths) {
        this.dependsOnTasks = Arrays.asList(paths)
    }

    File getVersionFile() {
        return versionPropsFile
    }

    int getVersionCode() {
        versionPropsFile = new File(project.buildFile.getParent() + "/version.properties")
        if (versionPropsFile.canRead()) {
            def Properties versionProps = new Properties()
            versionProps.load(new FileInputStream(versionPropsFile))
            if (versionProps['AI_VERSION_CODE'] == null) {
                versionProps['AI_VERSION_CODE'] = "1"
            }
            int code = Integer.valueOf(versionProps['AI_VERSION_CODE'].toString())
            return code
        } else {
            throw new GradleException("Could not read version.properties file in path \""
                    + versionPropsFile.getAbsolutePath() + "\" \r\n" +
                    "Please create this file and add it to your VCS (git, svn, ...).")
        }
    }

    def increase(BuildVersionExtension advancedVersioning) {
        System.out.println("Will increase version code")
        def versionPropsFile = versionFile
        if (versionPropsFile.canRead()) {
            def Properties versionProps = new Properties()
            versionProps.load(new FileInputStream(versionPropsFile))
            def versionCode = advancedVersioning.codeOptions.versionCode

            versionProps['AI_VERSION_CODE'] = versionCode.toString()
            versionCode += 1
            versionProps['AI_VERSION_CODE'] = versionCode.toString()
            versionProps.store(versionPropsFile.newWriter(), null)

        }
    }

    def needIncrease() {
        def needIncrease = false
        for (dependentTask in dependsOnTasks) {
            for (String taskName in project.gradle.startParameter.taskNames) {
                if (taskName.toLowerCase(Locale.ENGLISH).contains(dependentTask.toLowerCase(Locale.ENGLISH))) {
                    needIncrease = true
                }
            }
        }
        return needIncrease
    }
}
