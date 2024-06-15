package com.project.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

//ArchUnit Architektur Tests
@AnalyzeClasses(packages = "com.project")
class ArchitectureTest {

    private final JavaClasses importedClassesWithoutTests = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.project");

    private final JavaClasses importedClassesWithTests = new ClassFileImporter()
            .importPackages("com.project");

    @Test
    void testLayeredArchitecture() {
        Architectures.LayeredArchitecture layeredArchitecture = Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("Boundary").definedBy("..boundary..")
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Entity").definedBy("..entity..")
                .layer("DTO").definedBy("..dto..")
                .whereLayer("Boundary").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Controller").mayOnlyBeAccessedByLayers("Boundary")
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Entity").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("DTO").mayOnlyBeAccessedByLayers("Boundary", "Controller", "Entity");

        layeredArchitecture.check(importedClassesWithoutTests);
    }

    @Test
    void boundary_should_depend_on_controller() {
        ArchRule boundaryShouldUseController = classes().that().resideInAPackage("..boundary..")
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .because("Boundary should depend on Controller");

        ArchRule boundaryShouldNotUseEntityOrService = ArchRuleDefinition.noClasses().that().resideInAPackage("..boundary..")
                .should().dependOnClassesThat().resideInAnyPackage("..entity..", "..service..")
                .because("Boundary should not depend on Entity or Service");

        boundaryShouldUseController.check(importedClassesWithoutTests);
        boundaryShouldNotUseEntityOrService.check(importedClassesWithoutTests);
    }

    @Test
    void controller_should_depend_on_entity_dto_and_service() {
        ArchRule controllerShouldUseEntity = classes().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..entity..")
                .because("Controller should depend on Entity");

        ArchRule controllerShouldUseDTO = classes().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..dto..")
                .because("Controller should depend on DTO");

        ArchRule controllerShouldUseService = classes().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..service..")
                .because("Controller should depend on Service");

        ArchRule controllerShouldNotUseBoundary = ArchRuleDefinition.noClasses().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..boundary..")
                .because("Controller should not depend on Boundary");

        controllerShouldUseEntity.check(importedClassesWithoutTests);
        controllerShouldUseDTO.check(importedClassesWithoutTests);
        controllerShouldUseService.check(importedClassesWithoutTests);
        controllerShouldNotUseBoundary.check(importedClassesWithoutTests);
    }

    @Test
    void entity_should_depend_on_dto() {
        ArchRule entityShouldUseDTO = classes().that().resideInAPackage("..entity..")
                .should().dependOnClassesThat().resideInAPackage("..dto..")
                .because("Entity should depend on DTO");

        ArchRule entityShouldNotDependOnBoundaryControllerOrService = ArchRuleDefinition.noClasses().that().resideInAPackage("..entity..")
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..service..")
                .because("Entity should not depend on Boundary, Controller, or Service");

        entityShouldUseDTO.check(importedClassesWithoutTests);
        entityShouldNotDependOnBoundaryControllerOrService.check(importedClassesWithoutTests);
    }

    @Test
    void service_should_depend_on_nothing() {
        ArchRule serviceShouldDependOnNothing = ArchRuleDefinition.noClasses().that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..entity..", "..dto..")
                .because("Service should depend on nothing");

        serviceShouldDependOnNothing.check(importedClassesWithoutTests);
    }

    @Test
    void dto_should_depend_on_nothing() {
        ArchRule serviceShouldDependOnNothing = ArchRuleDefinition.noClasses().that().resideInAPackage("..dto..")
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..entity..", "..service")
                .because("Service should depend on nothing");

        serviceShouldDependOnNothing.check(importedClassesWithoutTests);
    }

    @Test
    void boundary_classes_should_have_specific_naming_convention() {
        ArchRule rule = classes().that().resideInAPackage("..boundary..")
                .should().haveSimpleNameEndingWith("Resource")
                .because("Boundary classes should end with 'Resource'");

        rule.check(importedClassesWithoutTests);
    }

    @Test
    void controller_classes_should_have_specific_naming_convention() {
        ArchRule rule = classes().that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .because("Controller classes should end with 'Controller'");

        rule.check(importedClassesWithoutTests);
    }

    @Test
    void dto_classes_should_have_specific_naming_convention() {
        ArchRule rule = classes().that().resideInAPackage("..dto..")
                .should().haveSimpleNameEndingWith("DTO")
                .because("DTO classes should end with 'DTO'");

        rule.check(importedClassesWithoutTests);
    }

    @Test
    void service_classes_should_have_specific_naming_convention() {
        ArchRule rule = classes().that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .because("Service classes should end with 'Service'");

        rule.check(importedClassesWithoutTests);
    }

    @Test
    void test_classes_should_have_specific_naming_convention() {
        ArchRule rule = classes().that()
                .resideInAnyPackage("..architecture..", "..boundary..", "..controller..", "..entity..", "..integration..")
                .and().areAnnotatedWith(Test.class).or().areAnnotatedWith(QuarkusTest.class)
                .should().haveSimpleNameEndingWith("Test")
                .because("Test classes should end with 'Test'");

        rule.check(importedClassesWithTests);
    }
}