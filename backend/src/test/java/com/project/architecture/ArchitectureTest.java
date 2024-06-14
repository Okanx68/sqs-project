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


@AnalyzeClasses(packages = "com.project")
class ArchitectureTest {

    @Test
    void testLayeredArchitecture() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

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

        layeredArchitecture.check(importedClasses);
    }

    @Test
    void boundary_should_depend_on_controller() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule boundaryShouldUseController = classes().that().resideInAPackage("..boundary..")
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .because("Boundary should depend on Controller");

        ArchRule boundaryShouldNotUseEntityOrService = ArchRuleDefinition.noClasses().that().resideInAPackage("..boundary..")
                .should().dependOnClassesThat().resideInAnyPackage("..entity..", "..service..")
                .because("Boundary should not depend on Entity or Service");

        boundaryShouldUseController.check(importedClasses);
        boundaryShouldNotUseEntityOrService.check(importedClasses);
    }

    @Test
    void controller_should_depend_on_entity_dto_and_service() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

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

        controllerShouldUseEntity.check(importedClasses);
        controllerShouldUseDTO.check(importedClasses);
        controllerShouldUseService.check(importedClasses);
        controllerShouldNotUseBoundary.check(importedClasses);
    }

    @Test
    void entity_should_depend_on_dto() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule entityShouldUseDTO = classes().that().resideInAPackage("..entity..")
                .should().dependOnClassesThat().resideInAPackage("..dto..")
                .because("Entity should depend on DTO");

        ArchRule entityShouldNotDependOnBoundaryControllerOrService = ArchRuleDefinition.noClasses().that().resideInAPackage("..entity..")
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..service..")
                .because("Entity should not depend on Boundary, Controller, or Service");

        entityShouldUseDTO.check(importedClasses);
        entityShouldNotDependOnBoundaryControllerOrService.check(importedClasses);
    }

    @Test
    void service_should_depend_on_nothing() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule serviceShouldDependOnNothing = ArchRuleDefinition.noClasses().that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..entity..", "..dto..")
                .because("Service should depend on nothing");

        serviceShouldDependOnNothing.check(importedClasses);
    }

    @Test
    void boundary_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule rule = classes().that().resideInAPackage("..boundary..")
                .should().haveSimpleNameEndingWith("Resource")
                .because("Boundary classes should end with 'Resource'");

        rule.check(importedClasses);
    }

    @Test
    void controller_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule rule = classes().that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .because("Controller classes should end with 'Controller'");

        rule.check(importedClasses);
    }

    @Test
    void dto_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule rule = classes().that().resideInAPackage("..dto..")
                .should().haveSimpleNameEndingWith("DTO")
                .because("DTO classes should end with 'DTO'");

        rule.check(importedClasses);
    }

    @Test
    void service_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.project");

        ArchRule rule = classes().that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .because("Service classes should end with 'Service'");

        rule.check(importedClasses);
    }

    @Test
    void test_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule rule = classes().that()
                .resideInAnyPackage("..architecture..", "..boundary..", "..controller..", "..entity..", "..integration..")
                .and().areAnnotatedWith(Test.class).or().areAnnotatedWith(QuarkusTest.class)
                .should().haveSimpleNameEndingWith("Test")
                .because("Test classes should end with 'Test'");

        rule.check(importedClasses);
    }

}