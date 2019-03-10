package com.ccomp.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import com.ccomp.model.Item;

public final class ItemTest {
	@Test
	public void sqlSelect() {
		List<Item> res;
		try {
			res = Item.loadAllItems();
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
			return;
		}
		Assert.assertEquals(2, res.size());
	}
}
