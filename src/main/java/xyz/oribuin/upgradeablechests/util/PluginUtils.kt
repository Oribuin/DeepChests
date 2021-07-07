package xyz.oribuin.upgradeablechests.util

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


object PluginUtils {

    fun compressItemStacks(items: Array<ItemStack>): ByteArray {
        if (items.isEmpty()) return ByteArray(0)

        var data: ByteArray

        ByteArrayOutputStream().use { os ->
            BukkitObjectOutputStream(os).use { oos ->
                oos.writeInt(items.size)
                for (itemStack in items) oos.writeObject(itemStack)
                data = os.toByteArray()
            }
        }

        return data
    }

    fun decompressItemStacks(data: ByteArray?): List<ItemStack> {
        if (data == null || data.contentEquals(ByteArray(0))) return emptyList()

        val items = mutableListOf<ItemStack>()

        ByteArrayInputStream(data).use { `is` ->
            BukkitObjectInputStream(`is`).use { ois ->
                val amount = ois.readInt()
                for (i in 0 until amount) items.add(ois.readObject() as ItemStack)
            }
        }

        return items
    }

}