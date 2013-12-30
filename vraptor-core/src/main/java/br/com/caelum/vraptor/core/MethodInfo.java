/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.caelum.vraptor.core;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.http.Parameter;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.http.ValuedParameter;

/**
 * Holder for method being invoked and parameters being passed.
 * 
 * @author Guilherme Silveira
 * @author Fabio Kung
 */
@RequestScoped
public class MethodInfo {

	private final ParameterNameProvider parameterNameProvider;

	private ControllerMethod controllerMethod;
	private ValuedParameter[] valuedParameters;
	private Object result;

	/**
	 * @deprecated CDI eyes only
	 */
	public MethodInfo() {
		this(null);
	}

	@Inject
	public MethodInfo(ParameterNameProvider parameterNameProvider) {
		this.parameterNameProvider = parameterNameProvider;
	}

	public ControllerMethod getControllerMethod() {
		return controllerMethod;
	}

	public void setControllerMethod(ControllerMethod controllerMethod) {
		createValuedParameter(controllerMethod);
		this.controllerMethod = controllerMethod;
	}

	public ValuedParameter[] getValuedParameters() {
		if (valuedParameters == null) {
			valuedParameters = new ValuedParameter[controllerMethod.getMethod().getTypeParameters().length];
		}
		return valuedParameters;
	}

	public void setParameter(int index, Object value) {
		valuedParameters[index].setValue(value);
	}

	public Object[] getParametersValues() {
		Object[] out = new Object[getValuedParameters().length];

		for (int i = 0; i < getValuedParameters().length; i++) {
			out[i] = getValuedParameters()[i].getValue();
		}

		return out;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	private void createValuedParameter(ControllerMethod controllerMethod) {
		if (controllerMethod != null && controllerMethod.getMethod() != null) {
			Parameter[] parameters = parameterNameProvider.parametersFor(controllerMethod.getMethod());
			valuedParameters = new ValuedParameter[parameters.length];
			for (int i = 0; i < valuedParameters.length; i++) {
				valuedParameters[i] = new ValuedParameter(parameters[i], null);
			}
		}
	}
}
