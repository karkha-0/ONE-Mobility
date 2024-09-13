/* 
 * Copyright 2024, Lund Univeristy, EIT, Security
 * Released under GPLv3. See LICENSE.txt for details. 
 */

package routing;


import java.util.HashSet;
import java.util.Set;

import java.util.ArrayList;
import java.util.List;

import core.DTNHost;
import core.Message;
import core.Settings;
import core.SimClock;

/**
 * InfectionRouter extending DirectDeliveryRouter with infection delay.
 */

public class InfectionRouter extends DirectDeliveryRouter {
    private boolean isInfected = false;
    private int installationTime;
    private double infectionStartTime = -1; // Time when the malware was received
    
    // Store the list of infected nodes
    //private static List<Integer> infectedNodesList = new ArrayList<>();
    private static Set<Integer> infectedNodesSet = new HashSet<>();
    

    public static final String INSTALLATION_TIME_S = "installationTime";

    public InfectionRouter(Settings s) {
        super(s);
        initializeSettings(s);
        //instance = this; 
    }

    /*public static InfectionRouter getInstance() {
        System.out.println(" InfectionRouter getInstance " );
        return instance;
    }*/

    @Override
    public int receiveMessage(Message m, DTNHost from) {
        // Check if this node is the intended recipient (final destination) for the message
        if (m.getTo().equals(this.getHost())) {
    
            // Now, check if the message is delivered successfully to non infected Nodes only
            if (!isInfected) {
                int result = super.receiveMessage(m, from);
        
                // If the message was successfully delivered, we should log or perform further actions
                if (result == RCV_OK){
                    //System.out.println("Message " + m.getId() + " was successfully delivered to node " + this.getHost().getAddress());
                    System.out.println("Message " + m.getId() + " was successfully delivered to node " 
                        + this.getHost().getAddress() + " from node " + from.getAddress());
                    
                    //If the get the malware and not yet infected, record the infection start time
                    infectionStartTime = SimClock.getTime();
                    // If the node is not yet infected, record the infection start time
                    /*if (!isInfected) {
                        infectionStartTime = SimClock.getTime();
                    }*/
                }
        
                return result;
            }
    
        }
        // If this node is not the intended recipient, discard the message
        return DENIED_OLD;
    }
    

    @Override
    public void update() {
        super.update();

        // If the node is not infected yet, check if the installation time has passed
        if (!isInfected && infectionStartTime > 0 && 
            SimClock.getTime() - infectionStartTime >= installationTime) {
            isInfected = true;
            System.out.println("Malware Infection time: " + (SimClock.getTime() - infectionStartTime));
            System.out.println("Node " + getHost().getAddress() + " is now infected.");

            // Add this infected node to the list of infected nodes
            //infectedNodesList.add(getHost().getAddress());
            //System.out.println("Infected nodes list size: " + infectedNodesList.size());
            //System.out.println("Infection Router: Updated hosts with infected nodes: " + infectedNodesList);

            // Add this infected node to the HashSet (no duplicates)
            infectedNodesSet.add(getHost().getAddress());
            System.out.println("Updated infected nodes: " + infectedNodesSet);
        }

        // If still not infected, return early
        if (!isInfected) {
            return;
        }

        // Check if a transfer can start
        if (isTransferring() || !canStartTransfer()) {
            return;
        }

        // Try delivering the message to the final recipient
        if (exchangeDeliverableMessages() != null) {
            return;
        }
    }

    @Override
    public InfectionRouter replicate() {
        return new InfectionRouter(this);
    }

    protected InfectionRouter(InfectionRouter r) {
        super(r);
        this.isInfected = r.isInfected;
        this.installationTime = r.installationTime;
        this.infectionStartTime = r.infectionStartTime;
        //this.messageEventGenerator = r.messageEventGenerator;
    }

    private void initializeSettings(Settings s) {
        if (s.contains(INSTALLATION_TIME_S)) {
            installationTime = s.getInt(INSTALLATION_TIME_S);
        } else {
            installationTime = 10;  // Default installation time
        }
    }

    //public static List<Integer> getInfectedNodesList() {
    public static Set<Integer> getInfectedNodesSet() {
        //System.out.println("getInfectedNodesList - infectedNodesList " + infectedNodesList);
        //return infectedNodesList;
        return infectedNodesSet;

    }


}