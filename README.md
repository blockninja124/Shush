# Shush

This mod allows the complete removal of console logs using Regex matches.

## Logs are usually important

If you are experiencing log spam, make sure to check with the mod author first! If you need to report an issue to **any** mod, please remove this mod first! Mod authors don't want tampered with logs when trying to find an issue.

## Then why does this exist

Sometimes, you might experience a log spamming your console that you know isn't a problem. Perhaps a dev forgot to remove a print statement somewhere, or the error simply isn't relevant to your situation. Sometimes it can even slow down your launcher of choice with the amount of logs. Normally, you just have to put up with it. But this mod provides an alternative.

## What does it offer

In the mod config, you can define a list of regex patterns. If any log matches a pattern, it will be prevented from outputting. This works for both `Log4j` messages (e.g. `[hh:mm:ss] [thread/LEVEL] [classpath]: message`) _and_ any un-conventional `System.out` messages (much harder to trace back to a particular mod, but luckily much less common).

## What on earth is Regex

Regex is short for "Regular Expression". Regular expressions are a sequence of characters that can be used to sort through text data. I highly recommend [RegExr.com](https://regexr.com/) for experimenting with Regex