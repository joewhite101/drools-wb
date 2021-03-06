/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.rule.client.widget;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.oracle.MethodInfo;
import org.drools.workbench.models.datamodel.rule.ExpressionCollection;
import org.drools.workbench.models.datamodel.rule.ExpressionField;
import org.drools.workbench.models.datamodel.rule.ExpressionGlobalVariable;
import org.drools.workbench.models.datamodel.rule.ExpressionMethod;
import org.drools.workbench.models.datamodel.rule.ExpressionPart;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;

public class ExpressionPartHelper {

    public static void getExpressionPartForMethod( final AsyncPackageDataModelOracle oracle,
                                                   final String factName,
                                                   final String methodName,
                                                   final Callback<ExpressionPart> callback ) {
        oracle.getMethodInfo( factName,
                              methodName,
                              new Callback<MethodInfo>() {
                                  @Override
                                  public void callback( final MethodInfo mi ) {
                                      if ( DataType.TYPE_COLLECTION.equals( mi.getGenericType() ) ) {
                                          callback.callback( new ExpressionCollection( methodName,
                                                                                       mi.getReturnClassType(),
                                                                                       mi.getGenericType(),
                                                                                       mi.getParametricReturnType() ) );
                                      } else {
                                          callback.callback( new ExpressionMethod( mi.getName(),
                                                                                   mi.getReturnClassType(),
                                                                                   mi.getGenericType() ) );
                                      }
                                  }
                              } );
    }

    public static void getExpressionPartForField( final AsyncPackageDataModelOracle oracle,
                                                  final String factName,
                                                  final String fieldName,
                                                  final Callback<ExpressionPart> callback ) {
        String fieldClassName = oracle.getFieldClassName( factName, fieldName );
        String fieldGenericType = oracle.getFieldType( factName, fieldName );
        if ( DataType.TYPE_COLLECTION.equals( fieldGenericType ) ) {
            String fieldParametricType = oracle.getParametricFieldType( factName,
                                                                        fieldName );
            callback.callback( new ExpressionCollection( fieldName,
                                                         fieldClassName,
                                                         fieldGenericType,
                                                         fieldParametricType ) );
        } else {
            callback.callback( new ExpressionField( fieldName,
                                                    fieldClassName,
                                                    fieldGenericType ) );
        }
    }

    public static void getExpressionPartForGlobalVariable( final AsyncPackageDataModelOracle oracle,
                                                           final String varName,
                                                           final Callback<ExpressionPart> callback ) {
        String globalVarType = oracle.getGlobalVariable( varName );
        callback.callback( new ExpressionGlobalVariable( varName,
                                                         globalVarType,
                                                         globalVarType ) );
    }

}
