name := "test"

//#enable-plugin
enablePlugins(ParadoxMaterialThemePlugin)
//#enable-plugin

paradoxProperties in Compile ++= Map(
  "github.base_url" -> "https://github.com/jonas/paradox-material-theme"
)

paradoxMaterialTheme in Compile ~= {
  _.withCopyright("test-copyright")
   .withLogo("test-logo")
   .withFavicon("test-favicon")
}

def fileContains(file: File, texts: String*) = {
  assert(file.exists, s"${file.getAbsolutePath} did not exist")
  val content = IO.readLines(file)
  for (text <- texts)
    assert(
      content.exists(_.contains(text)),
      s"Did not find '$text' in ${file.getName}:\n${content.mkString("\n")}"
    )
}

TaskKey[Unit]("checkContent") := {
  val dest = (target in (Compile, paradox)).value

  fileContains(
    dest / "index.html",
    "Paradox Site", "Nicely themed", "mkdocs-material", "test-copyright",
    "test-logo", "test-favicon"
  )

  fileContains(
    dest / "search" / "search_index.json",
    "Paradox Site", "Nicely themed"
  )
}
