package org.jetlinks.community.rule.engine.web;

import org.jetlinks.community.rule.engine.device.DeviceAlarmRule;
import org.jetlinks.community.rule.engine.entity.DeviceAlarmEntity;
import org.jetlinks.community.rule.engine.service.DeviceAlarmService;
import org.jetlinks.community.test.spring.TestJetLinksController;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import java.util.Date;


@WebFluxTest(DeviceAlarmController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeviceAlarmControllerTest extends TestJetLinksController {
    private static final String BASE_URL="/device/alarm";
    private static final String TargetId="deviceId";
    private static final String Target="device";
    private static final String ID="test";

    @Test
    void getService() {
        new DeviceAlarmController(Mockito.any(DeviceAlarmService.class)).getService();
    }

    @Test
    @Order(1)
    void getAlarms() {
        client.get()
            .uri(BASE_URL+"/"+Target+"/"+TargetId)
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    @Test

    void saveAlarm() {
        DeviceAlarmEntity entity = new DeviceAlarmEntity();
        entity.setId(ID);
        entity.setCreateTime(new Date());
        entity.setName("test");
        entity.setAlarmRule(new DeviceAlarmRule());
        client.patch()
            .uri(BASE_URL+"/"+Target+"/"+TargetId)
            .bodyValue(entity)
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    @Test
    @Order(1)
    void startAlarm() {
        client.post()
            .uri(BASE_URL+"/"+ID+"/_start")
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    @Test
    @Order(2)
    void stopAlarm() {
        client.post()
            .uri(BASE_URL+"/"+ID+"/_stop")
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    @Test
    @Order(3)
    void deleteAlarm() {
        client.delete()
            .uri(BASE_URL+"/"+ID)
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }
}