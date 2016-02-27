

/*
#define ETH_DATA_MAX_SIZE						1500

//////////////////////////////////////////////////////////////////////////

#define ANSWER_ERROR_DEST_ADDRESS			-600
#define ANSWER_ERROR_SOUR_ADDRESS			-601
#define ANSWER_ERROR_DATA_LENGTH			-602
#define ANSWER_ERROR_CRC					-603


typedef struct FrameDataField
{
	bool bMulticast;
	int nMulticastNum;
	bool bNoMulticast;
	bool bAnswer;
	bool bRead;
	bool bFIFO;
	unsigned char  ucAddress[2];

	int nSerialNumber;
	int nLength;
	unsigned char ucData[ETH_DATA_MAX_SIZE - 5];
	void reset()
	{
		memset(this, 0, sizeof(FrameDataField));
	}
}FRAMEDATAFIELD;

*/
