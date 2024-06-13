package com.project.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


@AnalyzeClasses(packages = "com.project")
class ArchitectureTest {

    @Test
    void boundary_should_depend_on_controller_and_dto() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule boundaryShouldUseController = ArchRuleDefinition.classes().that().resideInAPackage("..boundary..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .because("Boundary should depend on Controller");

        ArchRule boundaryShouldNotUseEntityOrService = ArchRuleDefinition.noClasses().that().resideInAPackage("..boundary..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAnyPackage("..entity..", "..service..")
                .because("Boundary should not depend on Entity or Service");

        boundaryShouldUseController.check(importedClasses);
        boundaryShouldNotUseEntityOrService.check(importedClasses);
    }

    @Test
    void controller_should_depend_on_entity_dto_and_service() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule controllerShouldUseEntity = ArchRuleDefinition.classes().that().resideInAPackage("..controller..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAPackage("..entity..")
                .because("Controller should depend on Entity");

        ArchRule controllerShouldUseDTO = ArchRuleDefinition.classes().that().resideInAPackage("..controller..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAPackage("..dto..")
                .because("Controller should depend on DTO");

        ArchRule controllerShouldUseService = ArchRuleDefinition.classes().that().resideInAPackage("..controller..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAPackage("..service..")
                .because("Controller should depend on Service");

        ArchRule controllerShouldNotUseBoundary = ArchRuleDefinition.noClasses().that().resideInAPackage("..controller..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAPackage("..boundary..")
                .because("Controller should not depend on Boundary");

        controllerShouldUseEntity.check(importedClasses);
        controllerShouldUseDTO.check(importedClasses);
        controllerShouldUseService.check(importedClasses);
        controllerShouldNotUseBoundary.check(importedClasses);
    }

    @Test
    void entity_should_depend_on_dto() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule entityShouldUseDTO = ArchRuleDefinition.classes().that().resideInAPackage("..entity..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAPackage("..dto..")
                .because("Entity should depend on DTO");

        ArchRule entityShouldNotDependOnBoundaryControllerOrService = ArchRuleDefinition.noClasses().that().resideInAPackage("..entity..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..service..")
                .because("Entity should not depend on Boundary, Controller, or Service");

        entityShouldUseDTO.check(importedClasses);
        entityShouldNotDependOnBoundaryControllerOrService.check(importedClasses);
    }

    @Test
    void service_should_depend_on_nothing() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule serviceShouldDependOnNothing = ArchRuleDefinition.noClasses().that().resideInAPackage("..service..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().dependOnClassesThat().resideInAnyPackage("..boundary..", "..controller..", "..entity..", "..dto..")
                .because("Service should depend on nothing");

        serviceShouldDependOnNothing.check(importedClasses);
    }

    @Test
    void boundary_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule rule = ArchRuleDefinition.classes().that().resideInAPackage("..boundary..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().haveSimpleNameEndingWith("Resource")
                .because("Boundary classes should end with 'Resource'");

        rule.check(importedClasses);
    }

    @Test
    void controller_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule rule = ArchRuleDefinition.classes().that().resideInAPackage("..controller..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().haveSimpleNameEndingWith("Controller")
                .because("Controller classes should end with 'Controller'");

        rule.check(importedClasses);
    }

    @Test
    void dto_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule rule = ArchRuleDefinition.classes().that().resideInAPackage("..dto..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().haveSimpleNameEndingWith("DTO")
                .because("DTO classes should end with 'DTO'");

        rule.check(importedClasses);
    }

    @Test
    void service_classes_should_have_specific_naming_convention() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.project");

        ArchRule rule = ArchRuleDefinition.classes().that().resideInAPackage("..service..")
                .and().areNotAnnotatedWith(Test.class).and().areNotAnnotatedWith(QuarkusTest.class)
                .should().haveSimpleNameEndingWith("Service")
                .because("Service classes should end with 'Service'");

        rule.check(importedClasses);
    }
}