package com.pmitseas.devicemgmt.service;

import com.pmitseas.devicemgmt.event.ActionEvent;
import com.pmitseas.devicemgmt.model.CommandMessage;
import com.pmitseas.devicemgmt.model.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceMgmtServiceImpl implements DeviceMgmtService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void handleMessageFromDevice(ResponseMessage message){
        //Custom logic here
        log.info("Message Contents: {}", message.toString());
    }

    @Override
    public void sendMessageToDevice(ActionEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);

        String client = event.getDestination();

        /*
         * Create a dummy message to send to client
         */
        CommandMessage message = CommandMessage.builder()
                .time(LocalDateTime.now().toString())
                .command(event.getCommand())
                .args(event.getArgs())
                .build();

        log.info("Sending message to device: {}", client);
        simpMessagingTemplate.convertAndSendToUser(client, "/queue/device", message, headerAccessor.getMessageHeaders());
    }


}
