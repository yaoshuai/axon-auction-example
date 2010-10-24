/*
 * Copyright (c) 2010. Axon Auction Example
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuin.auction.command.server;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.caucho.burlap.io.BurlapInput;
import com.caucho.burlap.io.BurlapOutput;

//TESTCODE:BEGIN
public final class VersioningTest {

	private File dataDir;

	@Before
	public final void setUp() {
		dataDir = new File("src/test/data/");
	}

	@After
	public final void tearDown() {
		dataDir = null;
	}

	@Test
	public final void testVersion() throws IOException {

		final File file = new File(dataDir, "RegisterUserCommandV99.xml");

		// Create local command with the same data as the serialized one
		final RegisterUserCommand command = new RegisterUserCommand("test", "12345678",
		        "abc@def.ghi");

		// Read serialized command that has only a different version number
		final RegisterUserCommand result = (RegisterUserCommand) deserialize(file);

		// ASSERT
		assertThat(command.getInstanceVersion()).isEqualTo(1);
		assertThat(command.getClassVersion()).isEqualTo(1);
		assertThat(command.isSameVersion()).isTrue();
		assertThat(result.getInstanceVersion()).isEqualTo(99);
		assertThat(result.getClassVersion()).isEqualTo(1);
		assertThat(result.isSameVersion()).isFalse();
		assertThat(result.getUserName()).isEqualTo(command.getUserName());
		assertThat(result.getPassword()).isEqualTo(command.getPassword());
		assertThat(result.getEmail()).isEqualTo(command.getEmail());

	}

	private void serialize(final File file, final Object object) throws IOException {
		final BurlapOutput out = new BurlapOutput(new FileOutputStream(file));
		try {
			out.writeObject(object);
		} finally {
			out.close();
		}
	}

	private Object deserialize(final File file) throws IOException {

		final BurlapInput in = new BurlapInput(new FileInputStream(file));
		try {
			return in.readObject();
		} finally {
			in.close();
		}

	}

}
// TESTCODE:END
