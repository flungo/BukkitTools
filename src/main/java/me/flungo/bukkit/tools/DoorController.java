/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.flungo.bukkit.tools;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;

/**
 *
 * @author Fabrizio
 */
public class DoorController {

	private final Block topBlock;
	private final Door topDoor;
	private final Block botBlock;
	private final Door botDoor;

	public DoorController(Block door) throws IllegalArgumentException {
		if (!(door.getState().getData() instanceof Door)) {
			throw new IllegalArgumentException("Block must represent a door.");
		}
		if (door.getRelative(BlockFace.UP).getType().equals(door.getType())) {
			topBlock = door.getRelative(BlockFace.UP);
			botBlock = door;
		} else if (door.getRelative(BlockFace.DOWN).getType().equals(door.getType())) {
			topBlock = door;
			botBlock = door.getRelative(BlockFace.DOWN);
		} else {
			throw new IllegalArgumentException("Door doesn't seem to be a full door?");
		}
		topDoor = (Door) topBlock.getState().getData();
		botDoor = (Door) botBlock.getState().getData();
	}

	public boolean isOpen() {
		return botDoor.isOpen();
	}

	public void setOpen(boolean isOpen) {
		botDoor.setOpen(isOpen);
	}

	public void setFacingDirection(BlockFace face) {
		botDoor.setFacingDirection(face);
	}

	public BlockFace getFacing() {
		return botDoor.getFacing();
	}

	public BlockFace getHingeCorner() {
		return topDoor.getHingeCorner();
	}

}
