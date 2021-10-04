import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

class PurchaseOrdersServer extends CyclicBehaviour {
    private BookSellerAgent bookSellerAgent;

    public PurchaseOrdersServer(BookSellerAgent agent)
    {
        super(agent);
        this.bookSellerAgent = agent;
    }
    
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            String title = msg.getContent();
            ACLMessage reply = msg.createReply();

            Integer price = (Integer) bookSellerAgent.catalogue.remove(title);
            if (price != null) {
                reply.setPerformative(ACLMessage.INFORM);
                System.out.println(title + " sold to agent " + msg.getSender().getName());
            }
            else {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("not-available");
            }
            myAgent.send(reply);
        }
        else {
            block();
        }
    }
}