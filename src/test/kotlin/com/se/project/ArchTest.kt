package com.se.project

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchTest {

    @Test
    fun servicesAndRepositoriesShouldNotDependOnWebLayer() {

        val importedClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.se.project")

        noClasses()
            .that()
                .resideInAnyPackage("com.se.project.service..")
            .or()
                .resideInAnyPackage("com.se.project.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.se.project.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses)
    }
}
