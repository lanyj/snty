package cn.lanyj.snty.common.codec;

import java.io.Serializable;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

public class DefaultMessageToMessageCodec extends MessageToMessageCodec<ByteBuf, Serializable> {

	private static final short MAGIC_CODE = (short) 0x9793;

	Serializer serializer = SerializerManager.getSerializer(SerializerManager.Hessian2);

	@Override
	protected void encode(ChannelHandlerContext ctx, Serializable msg, List<Object> out) throws Exception {
		byte[] buf = serializer.serialize(msg);

		ByteBuf byteBuf = ctx.alloc().buffer(buf.length + Short.BYTES);
		byteBuf.writeShort(MAGIC_CODE);
		byteBuf.writeBytes(buf);
		out.add(byteBuf);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		msg.markReaderIndex();
		short magic = msg.readShort();
		if (magic == MAGIC_CODE) {
			byte[] buf = new byte[msg.readableBytes()];
			msg.readBytes(buf);
			Serializable value = serializer.deserialize(buf);
			out.add(value);
		} else {
			msg.resetReaderIndex();
		}
	}

}
