# FlipFit – Build & Run in STS

## Fix "missing required source folder: 'com.flipfit.business/src'"

This error means the **Build Path** references an invalid source folder. Fix it first:

1. Right‑click project **JEDI-CHI-DEVELOPMENT-FLIPKART** → **Build Path → Configure Build Path**.
2. Open the **Source** tab.
3. You should have **only one** source folder: **`JEDI_CHI_Developmeent_JAVA_POS/src`**.
4. If you see **`com.flipfit.business/src`** or any other invalid entry → **Remove** it.
5. If the correct source folder is missing → **Add Folder** → select **`JEDI_CHI_Developmeent_JAVA_POS`** → **`src`** → OK.
6. **Default output folder** must be **`JEDI_CHI_Developmeent_JAVA_POS/bin`**.
7. **Apply and Close**.
8. **Project → Clean…** → select the project → **Clean**.

---

## Why "Could not find or load main class" / "UserServiceInterface cannot be resolved" happens

- Your **output folder** is `JEDI_CHI_Developmeent_JAVA_POS/bin`. STS runs from there.
- **Build path errors** or **failed build** → `bin` missing newer classes (e.g. `UserServiceImpl`, `UserServiceInterface`) → `ClassNotFoundException` or "cannot be resolved".
- Fix the build path (above), then **Clean + Build**.

---

## 1. Check if the build is actually succeeding

### Problems view
- **Window → Show View → Problems**
- Look for **red errors** (not just warnings) under your project.
- If you see errors like "The type X cannot be resolved", "package Y does not exist", fix those first. The build will not finish successfully until all errors are gone.

### Console when building
- **Project → Clean…** → select your project → **Clean**.
- Watch the **Console** view. You should see build messages.
- If the build fails, the Console will show compiler errors. Fix them, then Clean again.

---

## 2. Verify Build Path (source & output)

- Right‑click project → **Build Path → Configure Build Path**.
- **Source** tab:
  - `src` should be the only source folder.
  - **Default output folder** must be `JEDI_CHI_Developmeent_JAVA_POS/bin` (or `bin`).
- **Libraries** tab:
  - You should have a **JRE** (e.g. JavaSE-25 or your JDK). If it’s broken (red X), remove it and add **Add Library → JRE System Library** again.
- Click **Apply and Close**.

---

## 3. Clean and rebuild

1. **Project → Clean…**
2. Select **"Clean the following projects"** and your project.
3. Click **Clean**.
4. Ensure **Project → Build Automatically** is checked.
5. Wait a few seconds for the automatic build to finish.

---

## 4. Confirm that `bin` was updated

- In **Package Explorer**, expand **bin**.
- You should see:  
  `bin/com/flipfit/bean/`, `.../business/`, `.../client/`, `.../data/`.
- There must be **`bin/com/flipfit/client/GymApplicationClient.class`**.

If `bin` is still empty after a clean build, the build is still failing. Check the **Problems** view again and fix any remaining errors.

---

## 5. Run configuration

1. **Run → Run Configurations…**
2. Under **Java Application**, select your **GymApplicationClient** config (or create one).
3. **Main** tab:
   - **Project:** `JEDI_CHI_Developmeent_JAVA_POS`
   - **Main class:** `com.flipfit.client.GymApplicationClient`
4. **Classpath** tab: the **Bootstrap Entries** should include your project. The project uses `bin` as output, so no need to add `bin` manually.
5. **Apply** → **Run**.

---

## 6. Quick test

- Right‑click **`GymApplicationClient.java`** → **Run As → Java Application**.
- If you still get "Could not find or load main class", `bin` is almost certainly empty → go back to steps 1–4 and fix the build first.

---

## Summary

| Step | What to do |
|------|------------|
| 1 | **Problems** view: fix all red errors. |
| 2 | **Build Path**: `src` → source, `bin` → output; JRE ok. |
| 3 | **Project → Clean** → Clean, then let **Build Automatically** run. |
| 4 | Check **`bin/com/flipfit/client/GymApplicationClient.class`** exists. |
| 5 | **Run As → Java Application** on `GymApplicationClient.java`. |

The app will run only when the build succeeds and `bin` contains the compiled classes.
