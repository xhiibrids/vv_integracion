package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import 	org.junit.jupiter.api.Assertions;
import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {

	@Mock
	private static AuthDAO mockAuthdao;

	@Mock
	private static GenericDAO mockGenericdao;
	private SystemManager systemManager;

	private User validUser = new User("1", "Pepe", "Pepeperez", "ESPAÑA", new ArrayList<Object>());
	private static final String validId = "1";

	private static final String invalidId = "2";


	@BeforeEach
	void setup() throws OperationNotSupportedException {
		systemManager = new SystemManager(mockAuthdao, mockGenericdao);
		when(mockAuthdao.getAuthData(validId)).thenReturn(validUser);


	}

	@Test
	void testStartremotesystemvalidId() throws SystemManagerException, OperationNotSupportedException {
		when(mockGenericdao.getSomeData(validUser, "where id="+validId)).thenReturn(new ArrayList<Object>());
		Assertions.assertEquals(systemManager.startRemoteSystem(validId, validId), new ArrayList<Object>());

	}

	@Test
	void testStartremotesystemInvalidId() throws SystemManagerException, OperationNotSupportedException {
		when(mockGenericdao.getSomeData(validUser, "where id="+ invalidId)).thenThrow(new OperationNotSupportedException());
		Assertions.assertThrows(SystemManagerException.class, () -> systemManager.startRemoteSystem(validId,invalidId) );

	}

}
