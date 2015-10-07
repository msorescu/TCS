//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:33 PM
//

package net.toppro.components.mls.engine.client;


/**
* Class ByteBuffer implements a growable array bytes.
*/
public class ByteBuffer   
{
    public int size_Renamed_Field = 0;
    public byte[] buffer = null;
    /**
    * Default constructor
    */
    public ByteBuffer() throws Exception {
        size_Renamed_Field = 0;
        buffer = new byte[32];
    }

    /**
    * Constructor ByteBuffer for the specified string
    */
    //public ByteBuffer(System.String s)
    //{
    //    size_Renamed_Field = 0; buffer = new byte[32];
    //    for (int i = 0; i < s.Length; i++)
    //        append(s[i]);
    //}
    /**
    * Constructor empty ByteBuffer with specified size
    */
    public ByteBuffer(int _size) throws Exception {
        size_Renamed_Field = _size;
        buffer = new byte[_size];
    }

    /**
    * Constructor ByteBuffer for the specified byte array and size
    */
    public ByteBuffer(byte[] b, int _size) throws Exception {
        size_Renamed_Field = _size;
        buffer = new byte[size_Renamed_Field];
        Array.Copy(b, 0, buffer, 0, size_Renamed_Field);
    }

    /**
    * Sets value to cell
    */
    public byte set_Renamed(int index, byte value_Renamed) throws Exception {
        ensure_capacity(index);
        buffer[index] = value_Renamed;
        return value_Renamed;
    }

    /**
    * Returns the byte at the specified index
    */
    public byte get_Renamed(int index) throws Exception {
        return buffer[index];
    }

    /**
    * Returns byte's array
    */
    public byte[] get_Renamed() throws Exception {
        return buffer;
    }

    /**
    * Returns last byte and decrement size of buffer
    */
    public byte pop() throws Exception {
        return buffer[--size_Renamed_Field];
    }

    /**
    * Adds the byte to the end of this array, 
    *  @param value   the byte to be added.
    */
    public byte append(byte value_Renamed) throws Exception {
        set_Renamed(size_Renamed_Field,value_Renamed);
        return value_Renamed;
    }

    /**
    * Returns the number of bytes in this vector.
    */
    public int size() throws Exception {
        return size_Renamed_Field;
    }

    /**
    * Returns last byte
    */
    public byte last() throws Exception {
        return buffer[size_Renamed_Field - 1];
    }

    /**
    * Increases the capacity of this buffer, if necessary, to ensure
    * that it can hold at least the number of bytes specified by
    * the minimum capacity argument.
    */
    public void ensure_capacity(int index) throws Exception {
        if (index < size_Renamed_Field)
            return ;
         
        if (index < buffer.length)
        {
            size_Renamed_Field = index + 1;
            return ;
        }
         
        byte[] new_buffer = new byte[buffer.length * 2];
        Array.Copy(buffer, 0, new_buffer, 0, size_Renamed_Field);
        buffer = new_buffer;
        size_Renamed_Field = index + 1;
    }

}


