package mzgh.flags.testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import mzgh.flags.Status;

class Test_Status
{
	@Test
	void instantiateStatus()
	{
		Status status = new Status();
		assertNotNull(status);
		assertEquals(0x00000000, status.field());
	}
	@Test
	void instantiateWithInitValue_MIN()
	{
		int initValue = 0x00000000;
		Status status = new Status(initValue);
		assertNotNull(status);
		assertEquals(initValue, status.field());
	}
	@Test
	void instantiateWithInitValue_MAX()
	{
		int initValue = 0xFFFFFFFF;
		Status status = new Status(initValue);
		assertNotNull(status);
		assertEquals(initValue, status.field());
	}
	@Test
	void copyConstructor_goodValue()
	{
		Status st1 = new Status(0x00010001);
		Status st2 = new Status(st1);
		
		assertNotEquals(st1, st2);
		assertEquals(st1.field(), st2.field());
	}
	@Test
	void copyConstructor_nullValue()
	{
		Status status = new Status(null);

		assertNotNull(status);
		assertEquals(Status.ST_ALL_CLEAR, status.field());
	}
		
	@Test
	void setOneBit_BitAt1()
	{
		Status status = new Status();
		status.SetBits(0x00000001);
		assertEquals(0x00000001, status.field());
		
		status.SetBits(0x01000000);
		assertEquals(0x01000001, status.field());
	}
	@Test
	void setOneBit_BitAt0()
	{
		Status status = new Status(0x01000001);
		status.SetBits(0x00000001);
		assertEquals(0x01000001, status.field());
		
		status.SetBits(0x01000000);
		assertEquals(0x01000001, status.field());
	}
	@Test
	void setTwoBitsAtTheSameTime_BitsAt0()
	{
		Status status = new Status(0x00001111);
		status.SetBits(0x00110000);
		assertEquals(0x00111111, status.field());
	}
	@Test
	void setTwoBitsAtTheSameTime_BitsAt1()
	{
		Status status = new Status(0x00001111);
		status.SetBits(0x00001100);
		assertEquals(0x00001111, status.field());
	}
	@Test
	void setAnyNumberOfBits_0And1Mixed()
	{
		Status status = new Status(0x01101110);
		status.SetBits(0x11000011);
		assertTrue(status.AreBitsSet(0x11101111));
	}
	
	@Test
	void maskConjunction()
	{
		assertEquals(0x00001110, (0x00001100 | 0x00000010));
		
		int f1 = 0x01010100;
		int f2 = 0x10101000;
		
		assertEquals(0x11111100, (f1 | f2));
	}
	@Test
	void isOneBitSet()
	{
		Status status = new Status();
		status.SetBits(0x00010000);
		assertTrue(status.AreBitsSet(0x00010000));
		assertFalse(status.AreBitsSet(0x10000000));
	}
	@Test
	void areBitsSet()
	{
		Status status = new Status(0x11110011);
		assertTrue(status.AreBitsSet(0x01100001));
		assertFalse(status.AreBitsSet(0x10001000));
	}
	@Test
	void isAtLeastOneOfManyBitSet_SingleBit()
	{
		Status status = new Status(0x00001111);
		assertTrue(status.AreBitsSet(0x00000001));
		assertTrue(status.IsAtLeastOneBitSet(0x00000001));
	}
	@Test
	void isAtLeastOneOfManyBitSet_MultipleBits()
	{
		Status status = new Status(0x11110011);
		
		int mask1 = 0x00000001;
		int mask2 = 0x00100000;
		int mask3 = 0x00001000;
		int mask4 = 0x00000100;
		
		assertTrue(status.AreBitsSet(mask1));
		assertTrue(status.AreBitsSet(mask2));
		assertFalse(status.AreBitsSet(mask3));
		assertFalse(status.AreBitsSet(mask4));
		
		assertTrue(status.IsAtLeastOneBitSet(mask1 | mask2));
		assertTrue(status.IsAtLeastOneBitSet(mask1 | mask3));
		assertTrue(status.IsAtLeastOneBitSet(mask3 | mask2));
		assertFalse(status.IsAtLeastOneBitSet((mask3 | mask4)));
	}
	
	@Test
	void clrOneBit_BitAt0()
	{
		Status status = new Status(0x00001111);
		status.ClrBits(0x10000000);
		assertEquals(0x00001111, status.field());
	}
	@Test
	void clrOneBit_BitAt1()
	{
		Status status = new Status(0x00001000);
		status.ClrBits(0x00001000);
		assertEquals(0x00000000, status.field());
	}
	@Test
	void clrTwoBitsAtTheSameTime_BitsAt0()
	{
		Status status = new Status(0x00110001);
		status.ClrBits(0x00001100);
		assertEquals(0x00110001, status.field());
	}
	@Test
	void clrTwoBitsAtTheSameTime_BitsAt1()
	{
		Status status = new Status(0x00110001);
		status.ClrBits(0x00110000);
		assertEquals(0x00000001, status.field());
	}
	@Test
	void clrAnyNumberOfBits_0And1Mixed()
	{
		Status status = new Status(0x01101110);
		status.ClrBits(0x11000011);
		assertTrue(status.AreBitsSet(0x00101100));
	}
	@Test
	void clrAll()
	{
		Status status = new Status();
		status.SetBits(0x00000001);
		status.SetBits(0x00000010);
		status.SetBits(0x10000000);
		status.SetBits(0x00010000);
		assertEquals(0x10010011, status.field());
		
		int nbBitsCleared = status.ClearAll();
		assertEquals(4, nbBitsCleared);
		assertEquals(0x00000000, status.field());
	}
	
	@Test
	void isOneBitClr()
	{
		Status status = new Status(0x00001111);
		assertTrue(status.AreBitsClr(0x10000000));
		assertFalse(status.AreBitsClr(0x10000001));
	}
	@Test
	void areBitsClr()
	{
		Status status = new Status(0x11010011);
		assertTrue(status.AreBitsClr(0x00101000));
		assertFalse(status.AreBitsClr(0x1100010));
		assertFalse(status.AreBitsClr(0x10100000));
	}
	@Test
	void isAtLeastOneOfManyBitClr_SingleBit()
	{
		Status status = new Status(0x00001111);
		assertTrue(status.AreBitsClr(0x10000000));
		assertTrue(status.IsAtLeastOneBitClr(0x10000000));
	}
	@Test
	void isAtLeastOneOfManyBitClr_MultipleBits()
	{
		Status status = new Status(0x10110011);
		
		int mask1 = 0x01000000;
		int mask2 = 0x00000100;
		int mask3 = 0x00000001;
		int mask4 = 0x00100000;
		
		assertTrue(status.AreBitsClr(mask1));
		assertTrue(status.AreBitsClr(mask2));
		assertFalse(status.AreBitsClr(mask3));
		assertFalse(status.AreBitsClr(mask4));
		
		assertTrue(status.IsAtLeastOneBitClr(mask1 | mask2));
		assertTrue(status.IsAtLeastOneBitClr(mask1 | mask3));
		assertTrue(status.IsAtLeastOneBitClr(mask3 | mask2));
		assertFalse(status.IsAtLeastOneBitClr((mask3 | mask4)));
	}
	
	@Test
	void tglOneBit_BitAt0()
	{
		Status status = new Status(0x11110000);
		status.TglBits(0x00000001);
		assertEquals(0x11110001, status.field());
	}
	@Test
	void tglOneBit_BitAt1()
	{
		Status status = new Status(0x11110000);
		status.TglBits(0x01000000);
		assertEquals(0x10110000, status.field());
	}
	@Test
	void tglTwoBitsAtTheSameTime_BitsAt0()
	{
		Status status = new Status(0x11110000);
		status.TglBits(0x00000101);
		assertEquals(0x11110101, status.field());
	}
	@Test
	void tglTwoBitsAtTheSameTime_BitsAt1()
	{
		Status status = new Status(0x11110000);
		status.TglBits(0x10100000);
		assertEquals(0x01010000, status.field());
	}
	@Test
	void tglAnyNumberOfBits_0And1Mixed()
	{
		Status status = new Status(0x10011100);
		status.TglBits(0x10110001);
		assertEquals(0x00101101, status.field());
	}
	
	@Test
	void maskBits_OneBit()
	{
		Status status = new Status(0x11010110);
		assertEquals(0x10000000, status.MaskBits(0x10000000));
	}
	@Test
	void maskBits_SevenBytes()
	{
		Status status = new Status(0x11010111);
		assertEquals(0x11010110, status.MaskBits(0xFFFFFFF0));
	}
	@Test
	void maskBits_fullFFFF()
	{
		Status status = new Status(0x11010011);
		assertEquals(0x11010011, status.MaskBits(0xFFFFFFFF));
	}
	@Test
	void maskBits_full0000()
	{
		Status status = new Status(0x11010011);
		assertEquals(0x00000000, status.MaskBits(0x00000000));
	}
	
	@Test
	void convertToString_noSep_blockAt0()
	{
		Status status = new Status(0x00000001);
		assertTrue(
			status.ConvertToString("", 0).equals("0x00000000000000000000000000000001")
		);
	}
	@Test
	void convertToString_noSep_blockInfTo0()
	{
		Status status = new Status(0x00000001);
		assertNull(status.ConvertToString("_", -1));
	}
	@Test
	void convertToString_noSepImpliesBlockSizeIsNotUsed()
	{
		Status status = new Status(0x00000001);
		assertTrue(status.ConvertToString("", 4).equals("0x00000000000000000000000000000001"));
		assertTrue(status.ConvertToString("", 3).equals("0x00000000000000000000000000000001"));
		assertTrue(status.ConvertToString("", 12).equals("0x00000000000000000000000000000001"));
		assertTrue(status.ConvertToString("", 8).equals("0x00000000000000000000000000000001"));
	}
	@Test
	void convertToString_SepIsUnderScore_MixedBlockSizes()
	{
		Status status = new Status(0x00000001);
		assertTrue(status.ConvertToString("_", 2).equals("0x00_00_00_00_00_00_00_00_00_00_00_00_00_00_00_01"));
		assertTrue(status.ConvertToString("_", 4).equals("0x0000_0000_0000_0000_0000_0000_0000_0001"));
		assertTrue(status.ConvertToString("_", 3).equals("0x00000000000000000000000000000001"));
		assertTrue(status.ConvertToString("_", 12).equals("0x00000000000000000000000000000001"));
		assertTrue(status.ConvertToString("_", 8).equals("0x00000000_00000000_00000000_00000001"));
	}
}
