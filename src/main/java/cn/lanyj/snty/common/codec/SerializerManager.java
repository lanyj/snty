/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.lanyj.snty.common.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage all serializers.
 *
 * Notice: Serializer is different with Codec. Serializer is mainly used to
 * deserialize bytes to object, or serialize object to bytes. We can use
 * hessian, json, protocol buff etc. Codec mainly used to encode bytes or decode
 * bytes according to the protocol format.
 * 
 */
public class SerializerManager {
	private static final Logger logger = LoggerFactory.getLogger(SerializerManager.class);

	public static final byte Hessian2 = 0;
	private static final Serializer defaultSerializer;

	private static Serializer[] serializers = new Serializer[5];

	static {
		defaultSerializer = new HessianSerializer();
		addSerializer(Hessian2, defaultSerializer);
	}

	private SerializerManager() {
	}

	public static Serializer getSerializer(int idx) {
		return serializers[idx];
	}

	public static void addSerializerOrReplace(int idx, Serializer serializer) {
		if (serializers.length > idx && serializers[idx] != null) {
			logger.warn("Serializer replaced for index = {}, from {} to {}", idx, serializers[idx], serializer);
			serializers[idx] = serializer;
		} else {
			if (serializers.length <= idx) {
				Serializer[] newSerializers = new Serializer[idx + 5];
				System.arraycopy(serializers, 0, newSerializers, 0, serializers.length);
				serializers = newSerializers;
			}
			serializers[idx] = serializer;
		}
	}

	public static void addSerializer(int idx, Serializer serializer) {
		if (serializers.length > idx && serializers[idx] != null) {
			throw new IllegalArgumentException(
					"Serializer already registered for idx = " + idx + " with " + serializers[idx]);
		}
		if (serializers.length <= idx) {
			Serializer[] newSerializers = new Serializer[idx + 5];
			System.arraycopy(serializers, 0, newSerializers, 0, serializers.length);
			serializers = newSerializers;
		}
		serializers[idx] = serializer;
		logger.info("Serializer registered successfully for idx = {} as {}", idx, serializer);
	}

	public static Serializer getDefaultSerializer() {
		return defaultSerializer;
	}
}
