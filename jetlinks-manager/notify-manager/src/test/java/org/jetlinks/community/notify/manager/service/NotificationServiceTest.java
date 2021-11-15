package org.jetlinks.community.notify.manager.service;

import org.hswebframework.ezorm.core.StaticMethodReferenceColumn;
import org.hswebframework.ezorm.rdb.mapping.ReactiveQuery;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.hswebframework.ezorm.rdb.mapping.ReactiveUpdate;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.jetlinks.community.notify.manager.entity.Notification;
import org.jetlinks.community.notify.manager.entity.NotificationEntity;
import org.jetlinks.community.notify.manager.enums.NotificationState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Test
    void init() {
        ReactiveRepository<NotificationEntity, String> repository = Mockito.mock(ReactiveRepository.class);

        Mockito.when(repository.save(Mockito.any(Publisher.class)))
            .thenReturn(Mono.just(SaveResult.of(1, 0)));

        NotificationService service = new NotificationService() {
            @Override
            public ReactiveRepository<NotificationEntity, String> getRepository() {
                return repository;
            }
        };
        service.init();
    }

    @Test
    void subscribeNotifications() {
        NotificationService service = new NotificationService();
        Notification notification = new Notification();
        notification.setId("test");
        service.subscribeNotifications(notification);
    }

    @Test
    void findAndMarkRead() {
        ReactiveRepository<NotificationEntity, String> repository = Mockito.mock(ReactiveRepository.class);
        ReactiveQuery<NotificationEntity> query = Mockito.mock(ReactiveQuery.class);
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setState(NotificationState.unread);
        notificationEntity.setDataId("test");
        notificationEntity.setTopicName("test");
        notificationEntity.setId("test");

        Mockito.when(repository.createQuery())
            .thenReturn(query);
        Mockito.when(query.setParam(Mockito.any(QueryParamEntity.class)))
            .thenReturn(query);
        Mockito.when(query.fetch())
            .thenReturn(Flux.just(notificationEntity));

        ReactiveUpdate<NotificationEntity> update = Mockito.mock(ReactiveUpdate.class);
        Mockito.when(repository.createUpdate())
            .thenReturn(update);
        Mockito.when(update.set(Mockito.any(StaticMethodReferenceColumn.class), Mockito.any(Object.class)))
            .thenReturn(update);
        Mockito.when(update.where())
            .thenReturn(update);
        Mockito.when(update.in(Mockito.any(StaticMethodReferenceColumn.class), Mockito.any(Collection.class)))
            .thenReturn(update);
        Mockito.when(update.and(Mockito.any(StaticMethodReferenceColumn.class), Mockito.any(Object.class)))
            .thenReturn(update);
        Mockito.when(update.execute())
            .thenReturn(Mono.just(1));

        NotificationService service = new NotificationService() {
            @Override
            public ReactiveRepository<NotificationEntity, String> getRepository() {
                return repository;
            }
        };
        QueryParamEntity queryParamEntity = new QueryParamEntity();
        service.findAndMarkRead(queryParamEntity)
            .map(NotificationEntity::getTopicName)
            .as(StepVerifier::create)
            .expectNext("test")
            .verifyComplete();
    }
}