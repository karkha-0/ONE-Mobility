/* 
 * Copyright 2024, Lund Univeristy, EIT, Security
 * Released under GPLv3. See LICENSE.txt for details. 
 */

 package routing;

 import java.util.ArrayList;
 import java.util.List;
 
 import core.DTNHost;
 import core.Message;
 import core.Settings;
 import core.SimClock;

 
 /**
  * D2D extending DirectDeliveryRouter with malware installation delay.
  */
 
    public class D2DRouter extends DirectDeliveryRouter {
    public D2DRouter(Settings s) {
        super(s);
        System.out.println("--->D2DRouter instance created with host: " + this.getHost());
    }

    @Override
    public int receiveMessage(Message m, DTNHost from) {
        System.out.println("--->D2DRouter: receiveMessage called for host: " + getHost());
        return super.receiveMessage(m, from);
    }

    @Override
    public void update() {
        super.update();
        System.out.println("--->D2DRouter: update called for host: " + getHost());
    }

    @Override
    public D2DRouter replicate() {
        return new D2DRouter(this);
    }

    protected D2DRouter(D2DRouter r) {
        super(r);
        System.out.println("--->D2DRouter instance replicated");
    }
 }