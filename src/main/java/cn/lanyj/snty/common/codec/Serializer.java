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

import java.io.Serializable;

import io.netty.handler.codec.CodecException;

/**
 * Serializer for serialize and deserialize.
 * 
 */
public interface Serializer {
	/**
	 * Encode object into bytes.
	 * 
	 * @param obj target object
	 * @return serialized result
	 */
	byte[] serialize(final Serializable obj) throws CodecException;

	/**
	 * Decode bytes into Object.
	 * 
	 * @param data serialized data
	 */
	<T extends Serializable> T deserialize(final byte[] data) throws CodecException;
}
