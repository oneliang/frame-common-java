package com.oneliang.frame.section;

public class BlockWrapper {

	private int id = 0;
	private Block block = null;

	public BlockWrapper(int id, Block block) {
		this.id = id;
		this.block = block;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}
}
