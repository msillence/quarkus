package io.quarkus.arc.processor;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Type;
import org.jboss.jandex.Type.Kind;

import io.quarkus.arc.impl.Sets;

public class DecoratorInfo extends BeanInfo implements Comparable<DecoratorInfo> {

    private final InjectionPointInfo delegateInjectionPoint;
    private final Set<Type> decoratedTypes;

    DecoratorInfo(AnnotationTarget target, BeanDeployment beanDeployment, InjectionPointInfo delegateInjectionPoint,
            Set<Type> decoratedTypes, List<Injection> injections, int priority) {
        super(target, beanDeployment, BuiltinScope.DEPENDENT.getInfo(),
                Sets.singletonHashSet(Type.create(target.asClass().name(), Kind.CLASS)), new HashSet<>(), injections,
                null, null, false, Collections.emptyList(), null, false, null, priority, null);
        this.delegateInjectionPoint = delegateInjectionPoint;
        this.decoratedTypes = decoratedTypes;
    }

    public InjectionPointInfo getDelegateInjectionPoint() {
        return delegateInjectionPoint;
    }

    public Type getDelegateType() {
        return delegateInjectionPoint.getRequiredType();
    }

    public ClassInfo getDelegateTypeClass() {
        return getDeployment().getBeanArchiveIndex().getClassByName(getDelegateType().name());
    }

    public Set<AnnotationInstance> getDelegateQualifiers() {
        return delegateInjectionPoint.getRequiredQualifiers();
    }

    public Set<Type> getDecoratedTypes() {
        return decoratedTypes;
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(target.get().asClass().flags());
    }

    @Override
    public boolean isDecorator() {
        return true;
    }

    @Override
    public String toString() {
        return "DECORATOR bean [decoratedTypes=" + decoratedTypes + ", target=" + getTarget() + "]";
    }

    @Override
    public int compareTo(DecoratorInfo other) {
        return getTarget().toString().compareTo(other.getTarget().toString());
    }

}
