/* 
 * Copyright 2024, Lund Univeristy, EIT, Security
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package report;

import core.DTNHost;
import core.Message;
import core.MessageListener;

/**
 * Report for tracking detailed message transfers, including sender, receiver,
 * and timestamp of successful message delivery.
 */
public class DetailedMessageTransferReport extends Report implements MessageListener {
    public static String HEADER = "# time  sender  receiver  messageID  delivered";

    /**
     * Constructor.
     */
    public DetailedMessageTransferReport() {
        init();
    }

    @Override
    public void init() {
        super.init();
        write(HEADER);  // Write the header for the report
    }

    @Override
    public void messageTransferred(Message m, DTNHost from, DTNHost to, boolean success) {
        // Only track successful transfers
        if (success && !isWarmup() && !isWarmupID(m.getId())) {
            String timestamp = format(getSimTime());
            String log = timestamp + " " + from.getAddress() + " " + to.getAddress() + " " + m.getId() + " SUCCESS";
            write(log);  // Log successful transfer
        }
    }

    @Override
    public void newMessage(Message m) {
        // Handle new message event, if needed in the future (not relevant for now)
    }

    @Override
    public void messageDeleted(Message m, DTNHost where, boolean dropped) {
        // Log dropped or deleted messages if needed
    }

    @Override
    public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {
        // Optionally log aborted transfers (not implemented for simplicity)
    }

    @Override
    public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {
        // Optionally log when message transfer starts (not implemented for simplicity)
    }

    @Override
    public void done() {
        super.done();
    }
}
