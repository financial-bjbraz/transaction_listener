package br.com.bjbraz.tx.rootstock;

import br.com.bjbraz.tx.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

@Service
public class RootstockListener {

    private Logger logger = LoggerFactory.getLogger(RootstockListener.class);

    @Autowired
    private Sender sender;

    private final String ROUTING_KEY = "ethereum";

    @Value("${web3j.client-address}")
    private String RSK_NODE_ADDRESS;

    @Async("taskExecutor")
    public void listenToAddress(final String address ) {
        logger.info("Openning a new Listener to address : " + address);
        Web3j web3j = Web3j.build(new HttpService(RSK_NODE_ADDRESS));
        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            logger.info(clientVersion);

            logger.info("Getting blockNumber: " + web3j.ethBlockNumber().send().getBlockNumber());

        } catch(Exception e) {
            logger.error("Using the RskNode address : " + RSK_NODE_ADDRESS);
            logger.error("Error occured on RootstockListener " + e);
        }

        web3j.transactionFlowable().timeout(5, TimeUnit.MINUTES).subscribe(tx -> {
            logger.info("Listening new transactions...");
            if(tx.getFrom().equalsIgnoreCase(address) || tx.getTo().equalsIgnoreCase(address)){
                logger.info("TxIndex : " + tx.getTransactionIndex());
                logger.info("BlockHash : " + tx.getBlockHash());
                logger.info("BlockNumber : " + tx.getBlockNumber());
                logger.info("From : " + tx.getFrom());
                logger.info("ChainId : " + tx.getChainId());
                logger.info("Hash : " + tx.getHash());
                logger.info("Publickey : " + tx.getPublicKey());
                logger.info("Value Transactioned : " + tx.getValue());
                web3j.ethGetTransactionReceipt("0xb538cde07e39cf6e2d167f191d753b107dea72d2d853a91f2b1d0285bea534b7").sendAsync().thenAccept(receipt -> {
                    logger.info("Transaction Receipt received");

                    receipt.getTransactionReceipt().ifPresent( rcp -> {
                        logger.info("Receipt Status is ");
                        logger.info(rcp.getStatus());
                    });

                    logger.info("Transaction Receipt received: " + receipt.getTransactionReceipt());
                });
                //web3j.shutdown();
            }
        }, error -> {
            logger.info("Error received...");
            logger.error(error.getMessage());
            logger.info("Error received...");
        });

    }

}
