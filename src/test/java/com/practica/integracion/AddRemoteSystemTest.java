package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import javax.naming.OperationNotSupportedException;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddRemoteSystemTest {
    private static final String VALID_ID = "1";
    private static final String INVALID_ID = "2";
    private static final User USR = new User("1", "ttt", "ttt", "ttt", Collections.emptyList());

    private static AuthDAO mockAuthdao;
    private static GenericDAO mockGenericdao;

    private static SystemManager systemManager;

    @BeforeAll
    static void setUp() {
        mockAuthdao = mock(AuthDAO.class);
        mockGenericdao = mock(GenericDAO.class);
        systemManager = new SystemManager(mockAuthdao, mockGenericdao);
    }

    @Test
    void validInput() throws OperationNotSupportedException, SystemManagerException {
        when(mockAuthdao.getAuthData(VALID_ID)).thenReturn(USR);
        when(mockGenericdao.updateSomeData(USR, null)).thenReturn(true);
        InOrder inOrder = Mockito.inOrder(mockAuthdao, mockGenericdao);
        systemManager.addRemoteSystem(VALID_ID, null);

        inOrder.verify(mockAuthdao).getAuthData(VALID_ID);
        inOrder.verify(mockGenericdao).updateSomeData(USR, null);
    }

    @Test
    void dataUpdateFail() throws OperationNotSupportedException {
        when(mockAuthdao.getAuthData(VALID_ID)).thenReturn(USR);
        when(mockGenericdao.updateSomeData(USR, null)).thenReturn(false);
        InOrder inOrder = Mockito.inOrder(mockAuthdao, mockGenericdao);

        Assertions.assertThrows(SystemManagerException.class,
                () -> systemManager.addRemoteSystem(VALID_ID, null));

        inOrder.verify(mockAuthdao).getAuthData(VALID_ID);
        inOrder.verify(mockGenericdao).updateSomeData(USR, null);
    }

    @Test
    void invalidAuth() throws OperationNotSupportedException {
        when(mockAuthdao.getAuthData(INVALID_ID)).thenReturn(null);
        when(mockGenericdao.updateSomeData(null, null))
                .thenThrow(new OperationNotSupportedException("Usuario inválido"));
        InOrder inOrder = Mockito.inOrder(mockAuthdao, mockGenericdao);

        Assertions.assertThrows(SystemManagerException.class,
                () -> systemManager.addRemoteSystem(INVALID_ID, null));

        inOrder.verify(mockAuthdao).getAuthData(INVALID_ID);
        inOrder.verify(mockGenericdao).updateSomeData(null, null);
    }
}
