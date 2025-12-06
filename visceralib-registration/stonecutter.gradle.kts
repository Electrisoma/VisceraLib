val CI = System.getenv("CI") == "true"

plugins {
    id("dev.kikugie.stonecutter")
}

if (CI) stonecutter active null
else stonecutter active "1.21.1"