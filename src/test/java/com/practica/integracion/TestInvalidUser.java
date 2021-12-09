package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import javax.naming.OperationNotSupportedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestInvalidUser {
    @Mock
    private AuthDAO mockAuthDao;

    @Mock
    private GenericDAO mockGenericDao;
    private SystemManager systemManager;
    private static final String invalidId = "2";
    private static final String validId = "1";
    private InOrder inOrder;


    @BeforeEach
    void setup() {
        systemManager = new SystemManager(mockAuthDao, mockGenericDao);
        when(mockAuthDao.getAuthData(invalidId)).thenReturn(null);
        inOrder = Mockito.inOrder(mockAuthDao, mockGenericDao);
    }

    @Test
    void testStartRemoteSystemValidId() throws OperationNotSupportedException {
        when(mockGenericDao.getSomeData(null, "where id=" + validId)).thenThrow(new OperationNotSupportedException());
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem(invalidId, validId));
        inOrder.verify(mockAuthDao).getAuthData(invalidId);
        inOrder.verify(mockGenericDao).getSomeData(null, "where id=" + validId);
    }


    @Test
    void testStartRemoteSystemInvalidId() throws OperationNotSupportedException {
        when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(new OperationNotSupportedException());
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem(invalidId, invalidId));
        inOrder.verify(mockAuthDao).getAuthData(invalidId);
        inOrder.verify(mockGenericDao).getSomeData(null, "where id=" + invalidId);
    }

    @Test
    void testStopRemoteSystemValidId() throws OperationNotSupportedException {
        when(mockGenericDao.getSomeData(null, "where id=" + validId)).thenThrow(new OperationNotSupportedException());
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.stopRemoteSystem(invalidId, validId));
        inOrder.verify(mockAuthDao).getAuthData(invalidId);
        inOrder.verify(mockGenericDao).getSomeData(null, "where id=" + validId);
    }

    @Test
    void testStopRemoteSystemInvalidId() throws OperationNotSupportedException {
        when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(new OperationNotSupportedException());
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.stopRemoteSystem(invalidId, invalidId));
        inOrder.verify(mockAuthDao).getAuthData(invalidId);
        inOrder.verify(mockGenericDao).getSomeData(null, "where id=" + invalidId);
    }
    @Test
    void testDeleteRemoteSystemValidId() throws OperationNotSupportedException{

        when(mockGenericDao.deleteSomeData(any(User.class),eq(validId))).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(SystemManagerException.class,()->systemManager.deleteRemoteSystem(invalidId,validId));
        inOrder.verify(mockGenericDao).deleteSomeData(any(User.class),eq(validId));

    }

    @Test
    void testDeleteRemoteSystemInvalidId() throws OperationNotSupportedException {
        when(mockGenericDao.deleteSomeData(any(User.class),eq(invalidId))).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(SystemManagerException.class,
                () -> systemManager.deleteRemoteSystem(invalidId, invalidId));
        inOrder.verify(mockGenericDao).deleteSomeData(any(User.class),eq(invalidId));

    }


}

