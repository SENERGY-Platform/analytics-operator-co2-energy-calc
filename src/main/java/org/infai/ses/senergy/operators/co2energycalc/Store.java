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

import java.io.*;

public class Store implements Serializable {
    private Double lastEnergy, lastCo2, total;

    public static final String path = "/opt/data";
    public static final String file = "/store.bin";

    public Store() {
        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(path+file);
            ObjectInputStream ois = new ObjectInputStream(streamIn);
            Store old = (Store) ois.readObject();
            lastEnergy = old.lastEnergy;
            lastCo2 = old.lastCo2;
            total = old.total;
            ois.close();
        } catch (Exception e) {
            System.err.println("Could not load saved data: " + e.getMessage());
        } finally {
            try {
                if (streamIn != null) streamIn.close();
            } catch (IOException e) {
                System.err.println("Could not close save file: " + e.getMessage());
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Detected. Saving Progress...");
            FileOutputStream streamOut = null;
            try {
                new File(path).mkdir();
                streamOut = new FileOutputStream(path+file);
                ObjectOutputStream oos = new ObjectOutputStream(streamOut);
                oos.writeObject(this);
                oos.close();
            } catch (Exception e) {
                System.err.println("Could not save data: " + e.getMessage());
            } finally {
                try {
                    if (streamOut != null) streamOut.close();
                } catch (IOException e) {
                    System.err.println("Could not close save file: " + e.getMessage());
                }
            }
            System.out.println("Shutdown Hook Completed");
        }));
    }

    public Double getLastCo2() {
        return lastCo2;
    }

    public void setLastCo2(Double lastCo2) {
        this.lastCo2 = lastCo2;
    }

    public Double getLastEnergy() {
        return lastEnergy;
    }

    public void setLastEnergy(Double lastEnergy) {
        this.lastEnergy = lastEnergy;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
