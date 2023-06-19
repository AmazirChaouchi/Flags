package mzgh.flags;

public class Status
{	
	public Status()
	{
		field = ST_ALL_CLEAR;
	}
	public Status(int initValue)
	{
		field = initValue;
	}
	public Status(Status src)
	{
		field = (src != null)
			? src.field
			: ST_ALL_CLEAR;
	}
	
	public void SetBits(int mask)
	{
		field |= mask;
	}
	public boolean AreBitsSet(int mask)
	{
		return ((field & mask) == mask)
			? true
			: false;
	}
	public boolean IsAtLeastOneBitSet(int mask)
	{
		return ((field & mask) > 0)
			? true
			: false;
	}
	
	public void ClrBits(int mask)
	{
		field &= ~mask;
	}
	public int ClearAll()
	{
		int fCopy = field;
		
		int nbBitsCleared = 0;
		for(int i = 0; i < 32; i++)
		{
			if((fCopy & 0x00000001) == 0x00000001) nbBitsCleared++;
			fCopy >>= 1;
		}
		
		field = 0x00000000;
		
		return nbBitsCleared;
	}
	public boolean AreBitsClr(int mask)
	{
		return (((~field) & mask) == mask)
			? true
			: false;
	}
	public boolean IsAtLeastOneBitClr(int mask)
	{
		return (((~field) & mask) > 0)
			? true
			: false;
	}
	
	public void TglBits(int mask)
	{
		field ^= mask;
	}
	
	public int MaskBits(int mask)
	{
		return (field & mask);
	}
	
	public String ConvertToString(String separator, int blockSize)
	{		
		/* If blockSize < 0, there is an error */
		if(blockSize < 0) return null;

		/* Is block a power of 2 ? */
		int blockSizeCopy = blockSize;
		while((blockSizeCopy % 2) == 0 && blockSize >= 2)
		{
			blockSizeCopy /= 2;
		}
		boolean isPowerOf2 = (blockSizeCopy == 1) ? true : false;
		
		String str = "";
		int copy = field;
		for(int i = 0; i < 32; i++)
		{
			if(blockSize > 0 && isPowerOf2 == true && i > 0 && (i % blockSize) == 0)
			{
				str = separator + str;
			}
			
			str = (copy & 0x00000001) + str;
			copy >>= 1;
		}
		
		str = "0x" + str;
		
		System.out.println("bs : " + blockSize + "\tsep : " + separator + "\tstr : " + str);
		
		return str;
	}
	
	private int field;
	public int field() { return field; }
	
	public static final int ST_ALL_CLEAR = 0x00000000;
}
