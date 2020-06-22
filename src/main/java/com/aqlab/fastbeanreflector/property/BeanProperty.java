package com.aqlab.fastbeanreflector.property;

import com.aqlab.fastbeanreflector.factory.BeanPropertyFactory;

public abstract class BeanProperty<ObjectT> extends AbstractObjectProperty<ObjectT> {

    public static final BeanPropertyFactory FACTORY = BeanPropertyFactory.INSTANCE;

}
