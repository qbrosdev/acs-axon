package com.qbros.acs.api.commands;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.queries.AbsQuery;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.qbros.acs.api", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitecturalClassLevelTest {


    @ArchTest
    static ArchRule beImmutable =
            classes().that().resideInAnyPackage("..commands..", "..events..", "..queries..")
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .should().haveModifier(JavaModifier.FINAL)
                    .andShould().haveOnlyFinalFields();

    @ArchTest
    static ArchRule beCommand =
            classes().that().resideInAPackage("..commands..").and().areNotInterfaces()
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .should().bePublic()
                    .andShould().beAssignableTo(AbsCommand.class)
                    .andShould().haveSimpleNameEndingWith("Cmd");

    @ArchTest
    static ArchRule beEvent =
            classes().that().resideInAPackage("..events..").and().areNotInterfaces()
                    .should().bePublic()
                    .andShould().beAssignableTo(AbsEvent.class)
                    .andShould().haveSimpleNameEndingWith("Event");

    @ArchTest
    static ArchRule beQuery =
            classes().that().resideInAPackage("..queries..").and().areNotInterfaces()
                    .should().bePublic()
                    .andShould().beAssignableTo(AbsQuery.class)
                    .andShould().haveSimpleNameEndingWith("Query");

}


