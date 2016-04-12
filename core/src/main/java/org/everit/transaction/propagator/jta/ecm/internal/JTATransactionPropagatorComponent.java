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

import java.util.Hashtable;

import javax.transaction.TransactionManager;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.transaction.propagator.TransactionPropagator;
import org.everit.transaction.propagator.jta.JTATransactionPropagator;
import org.everit.transaction.propagator.jta.ecm.JTATransactionPropagatorConstants;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * ECM component for {@link TransactionPropagator} interface based on JTA Transaction Propagator.
 */
@Component(componentId = JTATransactionPropagatorConstants.COMPONENT_ID,
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit JTA Transaction Propagator",
    description = "Propagate JTA transactions easily with functional interfaces"
        + " and lambda expressions")
@ExtendComponent
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = JTATransactionPropagatorConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = JTATransactionPropagatorComponent.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration."
            + "It is used to easily identify the service registered by this component.") })
@ManualServices(@ManualService(TransactionPropagator.class))
public class JTATransactionPropagatorComponent {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_TRANSACTION_MANAGER = 2;

  private ServiceRegistration<TransactionPropagator> serviceRegistration;

  private TransactionManager transactionManager;

  /**
   * The activate method that registers a {@link JTATransactionPropagator} OSGi service.
   */
  @Activate
  public void activate(final ComponentContext<JTATransactionPropagatorComponent> componentContext) {

    TransactionPropagator transactionPropagator = new JTATransactionPropagator(transactionManager);

    Hashtable<String, Object> properties = new Hashtable<>();
    properties.putAll(componentContext.getProperties());

    serviceRegistration = componentContext.registerService(
        TransactionPropagator.class,
        transactionPropagator,
        properties);
  }

  /**
   * Unregisters the {@link JTATransactionPropagator} OSGi service.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = JTATransactionPropagatorConstants.ATTR_TRANSACTION_MANAGER,
      defaultValue = "",
      attributePriority = P02_TRANSACTION_MANAGER,
      label = "TransactionManager",
      description = "Filter expression for JTA TransactionManager OSGi service")
  public void setTransactionManager(final TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

}
