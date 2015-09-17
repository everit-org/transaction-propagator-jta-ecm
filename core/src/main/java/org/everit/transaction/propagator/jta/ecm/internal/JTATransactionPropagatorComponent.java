/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.transaction.propagator.jta.ecm.internal;

import java.util.function.Supplier;

import javax.transaction.TransactionManager;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.transaction.propagator.TransactionPropagator;
import org.everit.transaction.propagator.jta.JTATransactionPropagator;
import org.everit.transaction.propagator.jta.ecm.JTATransactionPropagatorConstants;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link TransactionPropagator} interface based on JTA Transaction Propagator.
 */
@Component(componentId = JTATransactionPropagatorConstants.COMPONENT_ID,
    configurationPolicy = ConfigurationPolicy.OPTIONAL)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = JTATransactionPropagatorConstants.DEFAULT_SERVICE_DESCRIPTION) })
@Service
public class JTATransactionPropagatorComponent implements TransactionPropagator {

  private TransactionManager transactionManager;

  private TransactionPropagator transactionPropagator;

  @Activate
  public void activate() {
    transactionPropagator = new JTATransactionPropagator(transactionManager);
  }

  @Override
  public <R> R mandatory(final Supplier<R> action) {
    return transactionPropagator.mandatory(action);
  }

  @Override
  public <R> R never(final Supplier<R> action) {
    return transactionPropagator.never(action);
  }

  @Override
  public <R> R notSupported(final Supplier<R> action) {
    return transactionPropagator.notSupported(action);
  }

  @Override
  public <R> R required(final Supplier<R> action) {
    return transactionPropagator.required(action);
  }

  @Override
  public <R> R requiresNew(final Supplier<R> action) {
    return transactionPropagator.requiresNew(action);
  }

  @ServiceRef(attributeId = JTATransactionPropagatorConstants.ATTR_TRANSACTION_MANAGER,
      defaultValue = "")
  public void setTransactionManager(final TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public <R> R supports(final Supplier<R> action) {
    return transactionPropagator.supports(action);
  }

}
