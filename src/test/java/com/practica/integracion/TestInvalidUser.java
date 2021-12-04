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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	@Mock
	private  AuthDAO mockAuthdao;

	@Mock
	private  GenericDAO mockGenericdao;
	private SystemManager systemManager;

	private User validUser = new User("2", "Pepe", "Pepeperez", "ESPAÑA", new ArrayList<Object>());

	private static final String invalidId = "2";
	private static final String validId = "1";


	@BeforeEach
	void setup() throws Exception {
		systemManager = new SystemManager(mockAuthdao, mockGenericdao);

		when(mockAuthdao.getAuthData(invalidId)).thenReturn(null);


	}

	@Test
	void teststartRemoteSystemvalidid() throws OperationNotSupportedException {
		when(mockGenericdao.getSomeData(null, "where id="+ validId)).thenThrow(new OperationNotSupportedException());
		Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem("2",validId) );

		//Assertions.assertThrows(OperationNotSupportedException.class, () -> systemManager.startRemoteSystem(validId,) ));

	}
	@Test
	void teststartRemoteSysteminvalid() throws SystemManagerException, OperationNotSupportedException {
		when(mockGenericdao.getSomeData(null, "where id="+invalidId)).thenThrow(new OperationNotSupportedException());

		Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem("2",invalidId) );

	}

}

/**
 * RELLENAR POR EL ALUMNO
 */
