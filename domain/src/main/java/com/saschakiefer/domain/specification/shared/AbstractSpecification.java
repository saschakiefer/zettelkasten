package com.saschakiefer.domain.specification.shared;

import com.saschakiefer.domain.exception.GenericSpecificationException;

public abstract class AbstractSpecification<T> implements Specification<T>{
    public abstract boolean isSatisfiedBy(T t);

    public abstract void check(T t) throws GenericSpecificationException;

    @Override
    public Specification<T> and(Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }
}
