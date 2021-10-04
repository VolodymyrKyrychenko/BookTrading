import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

class OfferRequestsServer extends CyclicBehaviour {
    private BookSellerAgent bookSellerAgent;

    public OfferRequestsServer(BookSellerAgent agent)
    {
        super(agent);
        this.bookSellerAgent = agent;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            String title = msg.getContent();
            ACLMessage reply = msg.createReply();

            Integer price = (Integer) bookSellerAgent.catalogue.get(title);
            if (price != null) {
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(String.valueOf(price.intValue()));
            }
            else {
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("not-available");
            }
            myAgent.send(reply);
        }
        else {
            block();
        }
    }
}