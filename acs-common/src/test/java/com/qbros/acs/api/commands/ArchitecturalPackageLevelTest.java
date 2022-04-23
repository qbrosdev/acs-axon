package com.qbros.acs.api.commands;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;


@AnalyzeClasses(packages = "com.qbros.acs.api", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitecturalPackageLevelTest {

    @ArchTest
    static final ArchRule commandPackage =
            ArchRuleDefinition.classes()
                    .that()
                    .haveSimpleNameEndingWith("Command")
                    .should()
                    .resideInAPackage("com.qbros.acs.api.commands..")
                    .because("It is obvious");

    @ArchTest
    static final ArchRule evensPackage =
            ArchRuleDefinition.classes()
                    .that()
                    .haveSimpleNameEndingWith("Event")
                    .should()
                    .resideInAPackage("com.qbros.acs.api.events..")
                    .because("It is obvious");


    @ArchTest
    static final ArchRule queriesPackage =
            ArchRuleDefinition.classes()
                    .that()
                    .haveSimpleNameEndingWith("Query")
                    .should()
                    .resideInAPackage("com.qbros.acs.api.queries..")
                    .because("It is obvious");
}
