/*
 * Copyright 2022 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infai.ses.senergy.operators.co2energycalc;

import org.infai.ses.senergy.exceptions.NoValueException;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.FlexInput;
import org.infai.ses.senergy.operators.Message;

public class Co2EnergyCalc extends BaseOperator {

    private final Store store = new Store();

    @Override
    public void run(Message message) {
        FlexInput energyInput = message.getFlexInput("energy");
        FlexInput co2Input = message.getFlexInput("co2");

        Double diff = null;
        try {
            Double energy = energyInput.getValue();
            if (store.getLastEnergy() != null) {
                diff = energy - store.getLastEnergy();
            }
            store.setLastEnergy(energy);
        } catch (NoValueException e) {
            // ok
        }

        Double co2 = null;
        try {
            co2 = co2Input.getValue();
            store.setLastCo2(co2);
        } catch (NoValueException e) {
            // ok
        }

        if (co2 == null) {
            co2 = store.getLastCo2();
        }

        if (diff != null && co2 != null) {
            Double total = store.getTotal();
            if (total == null) {
                total = 0.0;
            }
            total += diff * co2;
            store.setTotal(total);
            message.output("co2_g", total);
        }
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("energy");
        message.addFlexInput("co2");
        return message;
    }
}
