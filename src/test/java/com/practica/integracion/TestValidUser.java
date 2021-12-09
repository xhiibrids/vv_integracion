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

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestValidUser {

    private static final String validId = "1";
    private static final String invalidId = "2";
    @Mock
    private static AuthDAO mockAuthDao;
    @Mock
    private static GenericDAO mockGenericDao;
    private SystemManager systemManager;
    private final User validUser = new User("1", "Pepe", "Pepeperez", "ESPAÑA", new ArrayList<>());
    private InOrder inOrder;

    @BeforeEach
    void setup() {
        systemManager = new SystemManager(mockAuthDao, mockGenericDao);
        when(mockAuthDao.getAuthData(validId)).thenReturn(validUser);
        inOrder = Mockito.inOrder(mockAuthDao, mockGenericDao);
    }

    @Test
    void testStartRemoteSystemValidId() throws SystemManagerException, OperationNotSupportedException {
        when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(new ArrayList<>());
        Assertions.assertEquals(systemManager.startRemoteSystem(validId, validId), new ArrayList<>());
        inOrder.verify(mockAuthDao).getAuthData(validId);
        inOrder.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
    }

    @Test
    void testStartRemoteSystemInvalidId() throws OperationNotSupportedException {
        when(mockGenericDao.getSomeData(validUser, "where id=" + invalidId)).thenThrow(new OperationNotSupportedException());
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem(validId, invalidId));
        inOrder.verify(mockAuthDao).getAuthData(validId);
        inOrder.verify(mockGenericDao).getSomeData(validUser, "where id=" + invalidId);
    }

    @Test
    void testStopRemoteSystemValidId() throws OperationNotSupportedException, SystemManagerException {
        when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(new ArrayList<>());
        Assertions.assertEquals(systemManager.stopRemoteSystem(validId, validId), new ArrayList<>());
        inOrder.verify(mockAuthDao).getAuthData(validId);
        inOrder.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
    }

    @Test
    void testStopRemoteSystemInvalidId() throws OperationNotSupportedException {
        when(mockGenericDao.getSomeData(validUser, "where id=" + invalidId)).thenThrow(new OperationNotSupportedException());
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.stopRemoteSystem(validId, invalidId));
        inOrder.verify(mockAuthDao).getAuthData(validId);
        inOrder.verify(mockGenericDao).getSomeData(validUser, "where id=" + invalidId);
    }
    @Test
    void testDeleteRemoteSystemValidId() throws OperationNotSupportedException,SystemManagerException{
        when(mockGenericDao.deleteSomeData(validUser,validId)).thenReturn(true);
        systemManager.deleteRemoteSystem(validId,validId);
        inOrder.verify(mockGenericDao).deleteSomeData(validUser,validId);

    }

    @Test
    void testDeleteRemoteSystemInvalidId() throws OperationNotSupportedException {
        when(mockGenericDao.deleteSomeData(validUser, invalidId)).thenReturn(false);

        Assertions.assertThrows(SystemManagerException.class,
                () -> systemManager.deleteRemoteSystem(validId, invalidId));
        inOrder.verify(mockGenericDao).deleteSomeData(validUser,invalidId);

    }

}
