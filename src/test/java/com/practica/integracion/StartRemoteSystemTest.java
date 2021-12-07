package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartRemoteSystemTest {

    @Mock
    private static AuthDAO mockAuthDao;

    @Mock
    private static GenericDAO mockGenericDao;
    private SystemManager systemManager;
    private final User validUser = new User("1", "Pepe", "Pepeperez", "ESPAÑA", new ArrayList<>());
    private final User invalidUser = new User("2", "Juan", "Paez", "Francia", new ArrayList<>());
    private static final String validId = "1";
    private static final String invalidId = "2";


    @Test
    void testValidUserAndValidSystem() throws SystemManagerException, OperationNotSupportedException {
        when(mockAuthDao.getAuthData(validId)).thenReturn(validUser);
        when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(new ArrayList<>());
        InOrder ord = inOrder(mockAuthDao, mockGenericDao);
        systemManager = new SystemManager(mockAuthDao, mockGenericDao);
        Assertions.assertEquals(systemManager.startRemoteSystem(validId, validId), new ArrayList<>());
        ord.verify(mockAuthDao).getAuthData(validUser.getId());
        ord.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
    }

    @Test
    void testInvalidUserAndInvalidSystem() throws OperationNotSupportedException {
        when(mockAuthDao.getAuthData(invalidId)).thenReturn(null);
        when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(new OperationNotSupportedException());
        InOrder ord = inOrder(mockAuthDao, mockGenericDao);
        systemManager = new SystemManager(mockAuthDao, mockGenericDao);
        Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem(invalidId, invalidId));
        ord.verify(mockAuthDao).getAuthData(invalidId);
        ord.verify(mockGenericDao).getSomeData(null, "where id=" + invalidId);
    }

}
