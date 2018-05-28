package com.psa.kblog.entities

/**
 * This class represents a user in the system.
 *
 * @property id is the user identifier.
 * @property firstName is the first name of the user.
 * @property lastName is the last name of the user.
 */
data class User(val id: String,
                val firstName: String,
                val lastName: String)
