package com.qbros.services;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.queries.AbsQuery;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;

import java.util.List;
import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(packages = "com.qbros.services", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitecturalClassLevelTest {

    public static Set<String> acceptableAnnotations = Set.of(CommandHandler.class.getName(), EventSourcingHandler.class.getName());

    @ArchTest
    static ArchRule publicClassNamesAndAnnotations =
            classes().that().arePublic()
                    .should(publicHaveSpecificAnnotations());

    @ArchTest
    static ArchRule eventHandlerMethodSignature =
            methods().that().haveFullName("on")
                    .should().beAnnotatedWith(EventSourcingHandler.class)
                    .andShould().bePublic()
                    .andShould().haveRawReturnType(Void.class)
                    .andShould().haveRawParameterTypes(isSubClassOf(AbsEvent.class));

    @ArchTest
    static ArchRule queryHandlerMethodSignature =
            methods().that().haveFullName("handle")
                    .should().beAnnotatedWith(CommandHandler.class)
                    .andShould().bePublic()
                    .andShould().haveRawReturnType(Void.class)
                    .andShould().haveRawParameterTypes(isSubClassOf(AbsQuery.class));

    private static ArchCondition<JavaClass> publicHaveSpecificAnnotations() {

        return new ArchCondition<>("Public method should have certain annotation") {

            @Override
            public void check(final JavaClass clazz, final ConditionEvents events) {

                clazz.getMethods().stream()
                        .filter(javaMethod -> javaMethod.getModifiers().contains(JavaModifier.PUBLIC))
                        .forEach(publicMethod -> {
                            if (publicMethod.getAnnotations().stream()
                                    .noneMatch(pubMetAnno -> acceptableAnnotations.contains(pubMetAnno.getType().getName())))
                                throw new AssertionError(
                                        String.format("Public method '%s' in class '%s' is not annotated with valid annotations %s",
                                                publicMethod.getName(), clazz.getName(), acceptableAnnotations));
                        });
            }
        };
    }

    private static DescribedPredicate<List<JavaClass>> isSubClassOf(Class clazz) {

        return new DescribedPredicate<>("of sub-type: " + clazz.getName()) {
            @Override
            public boolean apply(List<JavaClass> input) {
                return 1 == input.size() && input.stream().allMatch(javaClass -> javaClass.isAssignableFrom(clazz));
            }
        };
    }
}
